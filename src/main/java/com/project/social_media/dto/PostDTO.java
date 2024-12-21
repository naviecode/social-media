package com.project.social_media.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.social_media.models.Posts;

import java.time.LocalDateTime;
import java.util.List;

public class PostDTO {
    private Long postId;
    private String content;
    private Posts.Privacy privacy;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private long reactionCount;
    private long commentCount;
    private List<byte[]> imagesData;
    private String fullNamePoster;
    private String avatarUrlPoster;
    private Long userId;

    @JsonProperty("isLiked")
    private boolean isLiked; // New field

    // Constructor
    public PostDTO(Long postId, String content, Posts.Privacy privacy, String avatarUrl, LocalDateTime createdAt, long reactionCount, long commentCount, List<byte[]> imagesData, String fullNamePoster, String avatarUrlPoster, boolean isLiked) {
        this.postId = postId;
        this.content = content;
        this.privacy = privacy;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
        this.reactionCount = reactionCount;
        this.commentCount = commentCount;
        this.imagesData = imagesData;
        this.fullNamePoster = fullNamePoster;
        this.avatarUrlPoster = avatarUrlPoster;
        this.isLiked = isLiked;
    }

    // Getters and Setters
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Posts.Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Posts.Privacy privacy) {
        this.privacy = privacy;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getReactionCount() {
        return reactionCount;
    }

    public void setReactionCount(long reactionCount) {
        this.reactionCount = reactionCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public List<byte[]> getImagesData() {
        return imagesData;
    }

    public void setImagesData(List<byte[]> imagesData) {
        this.imagesData = imagesData;
    }

    public String getFullNamePoster() {
        return fullNamePoster;
    }

    public void setFullNamePoster(String fullNamePoster) {
        this.fullNamePoster = fullNamePoster;
    }

    public String getAvatarUrlPoster() {
        return avatarUrlPoster;
    }

    public void setAvatarUrlPoster(String avatarUrlPoster) {
        this.avatarUrlPoster = avatarUrlPoster;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}