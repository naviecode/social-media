package com.project.social_media.services;

import com.project.social_media.models.Comments;
import com.project.social_media.models.Posts;
import com.project.social_media.models.Users;
import com.project.social_media.repository.CommentsRepository;
import com.project.social_media.repository.PostsRepository;
import com.project.social_media.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Transactional
    public Comments createComment(Long postId, Long userId, String content, Long parentCommentId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Posts post = postsRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        Comments comment = new Comments();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        comment.setParentComment(null); // Parent comment is null for top-level comments
        return commentsRepository.save(comment);
    }

    public long getCommentCountByPostId(Long postId) {
        return commentsRepository.countByPost_PostId(postId);
    }
}