package com.project.social_media.dto;

import com.project.social_media.models.Comments;

public class CommentDTO {
    private Long commentId;
    private Long postId;
    private Long userId;
    private String content;
    private Long parentCommentId;

    public CommentDTO() {
    }

    public CommentDTO(Comments comment) {
        this.commentId = comment.getCommentId();
        this.postId = comment.getPost().getPostId();
        this.userId = comment.getUser().getUserId();
        this.content = comment.getContent();
        this.parentCommentId = comment.getParentComment() != null ? comment.getParentComment().getCommentId() : null;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
}