package com.project.social_media.dto;

public class FriendWithUsernameDto {
    private Long friendId;
    private Long userId1;
    private Long userId2;
    private String status;
    private String fullName;
    private Long chatId;
    private String username;
    private Long unreadMessagesCount;
    private String lastMessageSender;

    public FriendWithUsernameDto(Long userId, String userName) {
        this.userId1 = userId;
        this.username = userName;
    }

    public FriendWithUsernameDto(Long friendId, Long userId1, Long userId2, String status, Long chatId) {
        this.friendId = friendId;
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.status = status;
        this.chatId = chatId;
    }

    public FriendWithUsernameDto(Long friendId, Long userId1, Long userId2, String status, String fullName) {
        this.friendId = friendId;
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.status = status;
        this.fullName = fullName;
    }

    public FriendWithUsernameDto(Long friendId, Long userId1, Long userId2, String status, String fullName, Long chatId, String username, Long unreadMessagesCount, String lastMessageSender) {
        this.friendId = friendId;
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.status = status;
        this.fullName = fullName;
        this.chatId = chatId;
        this.username = username;
        this.unreadMessagesCount = unreadMessagesCount;
        this.lastMessageSender = lastMessageSender;
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
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Long getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    public void setUnreadMessagesCount(Long unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }

    public String getLastMessageSender() {
        return lastMessageSender;
    }

    public void setLastMessageSender(String lastMessageSender) {
        this.lastMessageSender = lastMessageSender;
    }

}
