let postAuthor = null;
function openCommentPopup(postId, postAuthorName) {
    const commentPopupContainer = document.getElementById('comment-popup-container');
    document.getElementById('post-author-name').textContent = postAuthorName;
    postAuthor = postAuthorName;
    commentPopupContainer.style.display = 'block';

    fetch(`/api/comments/${postId}`)
        .then(response => response.json())
        .then(comments => {
            displayCommentsPosts(comments);
        })
        .catch(error => console.error('Error loading comments:', error));
}

function closeCommentPopup() {
    document.getElementById('comment-popup-container').style.display = 'none';
    let overlay = document.getElementById('overlay');
    overlay.style.display = 'none';
}

function displayCommentsPosts(comments) {
    const commentContainer = document.querySelector('.comment-popup-container .comments');
    commentContainer.innerHTML = '';

    const CommentPopupContainer = document.getElementById('comment-popup-container');
    let overlay = document.getElementById('overlay');

    // Create overlay if it doesn't exist
    if (!overlay) {
        overlay = document.createElement('div');
        overlay.id = 'overlay';
        document.body.appendChild(overlay);
    }

    // Process each comment and append to the container
    comments.forEach(comment => {
        const commentElement = createCommentElement(comment);
        commentContainer.appendChild(commentElement);
        CommentPopupContainer.style.display = 'block';
        overlay.style.display = 'block'; // Show the overlay

        // Close popup and hide overlay on clicking the overlay
        overlay.addEventListener('click', function () {
            CommentPopupContainer.style.display = 'none';
            overlay.style.display = 'none';
        });
    });
}

function createCommentElement(comment) {
    const commentElement = document.createElement('div');
    commentElement.className = 'comment';
    commentElement.dataset.commentId = comment.commentId;

    commentElement.innerHTML = `
        <div class="comment-header">
            <div class="align-center">
                <img alt="User profile picture" height="40" src="${comment.userAvatarUrl || '/default-avatar.png'}" width="40"/>
                <div class="comment-content">
                    <p class="name">${comment.userFullName}</p>
                    <p class="time">${formatTime(comment.createdAt)}</p>
                    <p class="text">${comment.content}</p>
                </div>
            </div>
        </div>
        <div class="actions">
            <div class="left">
                  <span class="action like-action ${comment.isLiked ? 'liked' : ''}" onclick="toggleLikeComment(${comment.commentId})" style="${comment.isLiked ? 'color: red;' : ''}">Thích</span> |
                <span class="action" onclick="addReplyInput(event, ${comment.commentId})">Phản hồi</span>
            </div>
            <div class="right">
                <span class="action"><i class="fas fa-heart"></i> <span class="like-count">${comment.likeCount}</span></span>
            </div>
        </div>
    `;

    // If there are replies, add them as nested comments
    if (comment.replies && comment.replies.length > 0) {
        const repliesContainer = document.createElement('div');
        repliesContainer.className = 'replies';
        comment.replies.forEach(reply => {
            const replyElement = createCommentElement(reply);
            repliesContainer.appendChild(replyElement);
        });
        commentElement.appendChild(repliesContainer);
    }

    return commentElement;
}

function formatTime(createdAt) {
    const now = new Date();
    const createdDate = new Date(createdAt);
    const diffHours = Math.floor((now - createdDate) / (1000 * 60 * 60));

    if (diffHours < 24) {
        return `${diffHours} giờ`;
    } else if (diffHours < 48) {
        return 'hai ngày trước';
    } else if (diffHours < 72) {
        return 'ba ngày trước';
    } else if (diffHours < 96) {
        return 'bốn ngày trước';
    } else if (diffHours < 120) {
        return 'năm ngày trước';
    } else if (diffHours < 144) {
        return 'sáu ngày trước';
    } else if (diffHours < 168) {
        return 'bảy ngày trước';
    } else {
        return createdDate.toLocaleDateString('vi-VN');
    }
}

function toggleLikeComment(commentId) {
    const userId = userLoginId;
    const commentElement = document.querySelector(`.comment[data-comment-id="${commentId}"]`);
    const likeActionElement = commentElement.querySelector('.like-action');
    const isLiked = likeActionElement.classList.contains('liked');

    const reaction = { commentId, userId, isLiked };

    stompClient.send(`/app/update-comment-reaction/${commentId}`, {}, JSON.stringify(reaction));

    if (isLiked) {
        likeActionElement.classList.remove('liked');
        likeActionElement.style.color = ''; // Remove red color
    } else {
        likeActionElement.classList.add('liked');
        likeActionElement.style.color = 'red'; // Add red color
    }

    const likeCountElement = commentElement.querySelector('.like-count');
    if (likeCountElement) {
        likeCountElement.textContent = parseInt(likeCountElement.textContent) + (isLiked ? -1 : 1);
    }
}

function addReplyInput(event) {
    const sanitizedFullName = userFullName.replace(/"/g, ''); // Removes all double quotes
    const comment = event.target.closest('.comment');
    const replyContainer = document.createElement('div');
    replyContainer.className = 'reply-input-container';
    replyContainer.innerHTML = `
        <div class="align-center">
                    <img alt="User profile picture" height="40" src="${userLogAvatarUrl}" width="40">
                    <div class="comment-content" style="outline: rgb(0 0 0) dashed 2px;background-color: #ffffff;">
                        <p class="my_comment_name" style="">${sanitizedFullName}</p>
                        <div class="footer">
                            <input placeholder="Viết bình luận..." type="text" style="" onkeypress="handleReplyKeyPress(event, ${comment.dataset.commentId})">
                        </div>
                    </div>
        </div>
    `;
    comment.appendChild(replyContainer);
}

let commentSubscription = null;

function handleReplyKeyPress(event, parentCommentId) {
    if (event.key === "Enter") {
        const inputElement = event.target;
        const content = inputElement.value.trim();
        const userId = userLoginId;
        if (content === "") return;

        const comment = { postId: currentPostId, userId, content, parentCommentId };

        // Unsubscribe from the previous subscription if it exists
        if (commentSubscription) {
            commentSubscription.unsubscribe();
        }

        // Subscribe to the topic to receive the response
        commentSubscription = stompClient.subscribe(`/topic/comments/${currentPostId}`, (response) => {
            const newComment = JSON.parse(response.body);

            const replyInputContainer = inputElement.closest('.reply-input-container');
            if (replyInputContainer) {
                replyInputContainer.remove();
            }

            const newCommentElement = createCommentElement(newComment);

            // Find the parent comment element and append the new comment to its replies container
            const parentCommentElement = document.querySelector(`.comment[data-comment-id="${parentCommentId}"]`);
            if (parentCommentElement) {
                let repliesContainer = parentCommentElement.querySelector('.replies');
                if (!repliesContainer) {
                    repliesContainer = document.createElement('div');
                    repliesContainer.className = 'replies';
                    parentCommentElement.appendChild(repliesContainer);
                }
                repliesContainer.appendChild(newCommentElement);
            } else {
                // If no parent comment is found, append to the main comments container
                const commentContainer = document.querySelector('.comment-popup-container .comments');
                commentContainer.appendChild(newCommentElement);
            }
        });

        // Send the comment
        stompClient.send(`/app/send-comment/${currentPostId}`, {}, JSON.stringify(comment));
    }
}