package com.project.social_media.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReactionDTO {
    private Long postId;
    private Long userId;
    @JsonProperty("isLiked")
    private boolean isLiked;

    // Getters and Setters
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

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }
}