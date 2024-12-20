package com.project.social_media.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentReactionDTO {
    private Long commentId;
    private Long userId;
    @JsonProperty("isLiked")
    private boolean isLiked;

    // Getters and Setters
    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}