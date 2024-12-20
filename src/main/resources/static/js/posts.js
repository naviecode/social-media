let page = 0;
const size = 10;
let isLoading = false;
let totalPostsLoaded = 0;
let noMorePosts = false; // To track if we've reached the end of posts
let stompClient;

function loadPosts() {
    if (isLoading || noMorePosts) return; // Prevent duplicate loading
    isLoading = true;
    $('#loading').show();

    $.ajax({
        url: `/api/posts/friend-and-own`,
        type: 'GET',
        data: { page: page, size: size },
        success: function(posts) {
            const socket = new SockJS('/chat');
            stompClient = Stomp.over(socket);

            stompClient.connect({}, () => {
                console.log("Connected to WebSocket");
                posts.forEach(function(post) {
                    stompClient.subscribe('/topic/update-reaction/' + post.postId, (message) => {
                        const reactionUpdate = JSON.parse(message.body);
                        console.log(reactionUpdate);
                        console.log(userLoginId);
                        console.log(reactionUpdate.userId);

                        const postElement = $(`.post[data-post-id="${reactionUpdate.postId}"]`);

                        if(userLoginId == reactionUpdate.userId){
                                const reactionIcon = reactionUpdate.isLiked
                                    ? `<div id="reaction-icon" class="reaction-icon">
                               <i class="fas fa-heart" style="color: red;"></i>
                           </div>`
                                    : `<div id="reaction-icon" class="reaction-icon">
                               <i class="far fa-heart"></i>
                           </div>`;

                            postElement.find('.reaction-icon').replaceWith(reactionIcon);
                            postElement.find('.like-count').text(reactionUpdate.reactionCount);
                        }


                    });
                    stompClient.subscribe('/topic/send-comment/' + post.postId, (message) => {
                        const newComment = JSON.parse(message.body);
                        displayComment(newComment);

                        const commentCountElement = document.querySelector(`.post[data-post-id="${newComment.postId}"] .comment-count`);
                        commentCountElement.textContent = parseInt(commentCountElement.textContent) + 1;
                    });
                });
            });

            const postContainer = $('#post-container');

            if (posts.length === 0) {
                noMorePosts = true;
                postContainer.append('<div class="no-more-posts">No more posts to see here</div>');
            } else {
                posts.forEach(post => {
                    let postElement;
                    const formattedDate = new Date(post.createdAt).toLocaleString('en-GB', {
                        day: '2-digit',
                        month: '2-digit',
                        year: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit',
                        hour12: false,
                    });

                    const reactionIcon = post.isLiked
                        ? `<div id="reaction-icon" class="reaction-icon">
                               <i class="fas fa-heart" style="color: red;"></i>
                           </div>`
                        : `<div id="reaction-icon" class="reaction-icon">
                               <i class="far fa-heart"></i>
                           </div>`;

                    if (post.imagesData.length === 0) {
                        // Case: No images
                        postElement = $(
                            `<div class="post" data-post-id="${post.postId}">
                                <div class="post-header">
                                    <img alt="Profile picture" height="32" src="/static/files/${post.avatarUrlPoster}" width="32"/>
                                    <span>${post.fullNamePoster}</span>
                                </div>
                                <div class="post-create-dt" style="font-weight: 300; color: grey; padding: 0 10px;">
                                    ${formattedDate}
                                </div>
                                <div class="post-contents">
                                    ${post.content}
                                </div>
                                <div class="post-actions">
                                    <div class="like-container">
                                        ${reactionIcon}
                                        <span class="like-count">${post.reactionCount}</span>
                                    </div>
                                    <div class="comment-container">
                                        <i class="far fa-comment"></i>
                                        <span class="comment-count">${post.commentCount}</span>
                                    </div>
                                </div>
                                <div class="post-footer">
                                    <img alt="Profile picture of user" height="32" src="/static/files/${post.avatarUrl}" width="32"/>
                                    <input id="comment-input-${post.postId}" placeholder="Add a comment..." type="text" onkeypress="handleCommentKeyPress(event, ${post.postId})"/>
                                </div>
                            </div>`
                        );
                    } else {
                        // Case: With images (carousel)
                        const images = post.imagesData.map(
                            (image, index) => `<img src="data:image/png;base64,${image}" style="display: ${index === 0 ? 'block' : 'none'};" />`
                        ).join('');

                        const dots = post.imagesData.length > 1
                            ? `<div class="post-carousel-dots">
                                   ${post.imagesData.map((_, index) => `<div class="post-dot ${index === 0 ? 'active' : ''}" onclick="goToPostImage(this, ${index})"></div>`).join('')}
                               </div>`
                            : '';

                        const navButtons = post.imagesData.length > 1
                            ? `<button class="post-nav-button post-prev" onclick="prevPostImage(this)">&#8249;</button>
                               <button class="post-nav-button post-next" onclick="nextPostImage(this)">&#8250;</button>`
                            : '';

                        postElement = $(
                            `<div class="post" data-post-id="${post.postId}">
                                <div class="post-header">
                                    <img alt="Profile picture" height="32" src="/static/files/${post.avatarUrlPoster}" width="32"/>
                                    <span>${post.fullNamePoster}</span>
                                </div>
                                <div class="post-image">
                                    <div class="post-carousel">
                                        ${images}
                                        ${navButtons}
                                        ${dots}
                                    </div>
                                </div>
                                <div class="post-actions">
                                    <div class="like-container">
                                        ${reactionIcon}
                                        <span class="like-count">${post.reactionCount}</span>
                                    </div>
                                    <div class="comment-container">
                                        <i class="far fa-comment"></i>
                                        <span class="comment-count">${post.commentCount}</span>
                                    </div>
                                </div>
                                <div class="post-create-dt" style="font-weight: 300; color: grey; padding: 0 10px;">
                                    ${formattedDate}
                                </div>
                                <div class="post-contents">
                                    <span>${post.fullNamePoster}</span> ${post.content}
                                </div>
                                <div class="post-footer">
                                    <img alt="Profile picture" height="32" src="/static/files/${post.avatarUrlPoster}" width="32"/>
                                    <input id="comment-input-${post.postId}" placeholder="Add a comment..." type="text" onkeypress="handleCommentKeyPress(event, ${post.postId})"/>
                                </div>
                            </div>`
                        );
                    }

                    postContainer.append(postElement);
                });

                totalPostsLoaded += posts.length;
                page++;
            }

            isLoading = false;
            $('#loading').hide();
        },
        error: function(error) {
            console.error('Error loading posts:', error);
            isLoading = false;
            $('#loading').hide();
        }
    });
}

$(window).on('scroll', function () {
    const scrollPosition = $(window).scrollTop() + $(window).height();
    const documentHeight = $(document).height();
    const threshold = 3250; // Pixels before the bottom to trigger loading

    if (scrollPosition >= documentHeight - threshold) {
        const posts = $('.post');
        if (posts.length >= totalPostsLoaded - 3) {
            loadPosts();
        }
    }
});

$(document).ready(function() {
    loadPosts();
});

// Carousel navigation functions
function prevPostImage(button) {
    const carousel = $(button).closest('.post-carousel');
    const images = carousel.find('img');
    let activeIndex = images.index(images.filter(':visible'));
    activeIndex = (activeIndex - 1 + images.length) % images.length;
    images.hide().eq(activeIndex).show();

    const dots = carousel.find('.post-dot');
    dots.removeClass('active').eq(activeIndex).addClass('active');
}

function nextPostImage(button) {
    const carousel = $(button).closest('.post-carousel');
    const images = carousel.find('img');
    let activeIndex = images.index(images.filter(':visible'));
    activeIndex = (activeIndex + 1) % images.length;
    images.hide().eq(activeIndex).show();

    const dots = carousel.find('.post-dot');
    dots.removeClass('active').eq(activeIndex).addClass('active');
}

function goToPostImage(dot, index) {
    const carousel = $(dot).closest('.post-carousel');
    const images = carousel.find('img');
    images.hide().eq(index).show();

    const dots = carousel.find('.post-dot');
    dots.removeClass('active').eq(index).addClass('active');
}

$(window).on('scroll', function() {
    const scrollPosition = $(window).scrollTop() + $(window).height();
    const documentHeight = $(document).height();
    const threshold = 1000;

    if (scrollPosition >= documentHeight - threshold) {
        const posts = $('.post');
        if (posts.length >= totalPostsLoaded - 3) {
            loadPosts();
        }
    }
});

$(document).on('click', '.reaction-icon', function() {
    const postElement = $(this).closest('.post');
    const postId = postElement.data('post-id');
    const isLiked = $(this).find('i').hasClass('fas');
    const userId = userLoginId;

    const reaction = { postId, userId, isLiked: isLiked };

    // Send WebSocket message to update reaction in real-time
    stompClient.send(`/app/update-reaction/${postId}`, {}, JSON.stringify(reaction));

    // Update the UI immediately for better user experience
    const reactionIcon = !isLiked
        ? `<div id="reaction-icon" class="reaction-icon">
               <i class="fas fa-heart" style="color: red;"></i>
           </div>`
        : `<div id="reaction-icon" class="reaction-icon">
               <i class="far fa-heart"></i>
           </div>`;

    postElement.find('.reaction-icon').replaceWith(reactionIcon);
    const likeCountElement = postElement.find('.like-count');
    likeCountElement.text(parseInt(likeCountElement.text()) + (isLiked ? -1 : 1));
});

function handleCommentKeyPress(event, postId) {
    if (event.key === "Enter") {
        sendComment(postId);
    }
}

function sendComment(postId) {
    const inputElement = document.getElementById(`comment-input-${postId}`);
    const content = inputElement.value.trim();
    const userId = userLoginId;
    if (content === "") return;

    const comment = { postId, userId, content, parentCommentId: null };

    stompClient.send(`/app/send-comment/${postId}`, {}, JSON.stringify(comment));
    inputElement.value = "";

    const commentCountElement = document.querySelector(`.post[data-post-id="${postId}"] .comment-count`);
    commentCountElement.textContent = parseInt(commentCountElement.textContent) + 1;

    alert("Your comment has been posted");
}

function displayComment(comment) {
    const commentElement = document.createElement("div");
    commentElement.classList.add("comment");
    commentElement.innerHTML = `
        <img alt="User profile picture" height="32" src="/static/files/${comment.avatarUrl}" width="32"/>
        <div class="comment-content">
            <span>${comment.fullName}</span> ${comment.content}
        </div>
    `;
    const postElement = document.querySelector(`.post[data-post-id="${comment.postId}"] .comments`);
    postElement.appendChild(commentElement);
}

$(document).on('click', '.comment-container', function() {
    const postId = $(this).closest('.post').data('post-id');
    const postAuthorName = $(this).closest('.post').find('.post-header span').text();
    currentPostId = postId;

    $('#post-author-name').text(postAuthorName);
    $('.comment-popup-container').show();

    $.ajax({
        url: `/api/comments/${postId}`,
        type: 'GET',
        success: function(comments) {
            displayCommentsPosts(comments);
            openCommentPopup(postId, postAuthorName);
        },
        error: function(error) {
            console.error('Error loading comments:', error);
        }
    });
});