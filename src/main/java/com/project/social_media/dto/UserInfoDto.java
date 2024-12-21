package com.project.social_media.dto;

public class UserInfoDto {
    private String fullName;
    private String username;
    public String avatarUrl;
    private long numberOfFriends;

    // Constructors, getters, and setters
    public UserInfoDto(String fullName, String username, long numberOfFriends, String avatarUrl) {
        this.fullName = fullName;
        this.username = username;
        this.numberOfFriends = numberOfFriends;
        this.avatarUrl = avatarUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getNumberOfFriends() {
        return numberOfFriends;
    }

    public void setNumberOfFriends(long numberOfFriends) {
        this.numberOfFriends = numberOfFriends;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
