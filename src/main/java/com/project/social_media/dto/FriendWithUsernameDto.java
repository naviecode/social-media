package com.project.social_media.dto;

public class FriendWithUsernameDto {
    private Long friendId;
    private Long userId1;
    private Long userId2;
    private String status;
    private String fullName;
    private Long chatId;

    public FriendWithUsernameDto(Long friendId, Long userId1, Long userId2, String status, String fullName) {
        this.friendId = friendId;
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.status = status;
        this.fullName = fullName;
        this.chatId = chatId;
    }

    public FriendWithUsernameDto(Long friendId, Long userId1, Long userId2, String status, String fullName, Long chatId) {
        this.friendId = friendId;
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.status = status;
        this.fullName = fullName;
        this.chatId = chatId;
    }

    public Long getFriendId() {
        return friendId;
    }
    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }
    public Long getUserId1() {
        return userId1;
    }
    public void setUserId1(Long userId1) {
        this.userId1 = userId1;
    }
    public Long getUserId2() {
        return userId2;
    }
    public void setUserId2(Long userId2) {
        this.userId2 = userId2;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getFullName() {
        return fullName;
    }
    public void getFullName(String fullName) {
        this.fullName = fullName;
    }
    public Long getChatId() {
        return chatId;
    }
    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

}
