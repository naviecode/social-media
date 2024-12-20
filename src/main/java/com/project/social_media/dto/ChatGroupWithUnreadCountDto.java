package com.project.social_media.dto;

public class ChatGroupWithUnreadCountDto {
    private Long chatId;
    private String groupName;
    private Long unreadMessagesCount;

    public ChatGroupWithUnreadCountDto(Long chatId, String groupName, Long unreadMessagesCount) {
        this.chatId = chatId;
        this.groupName = groupName;
        this.unreadMessagesCount = unreadMessagesCount;
    }

    // Getters and setters
    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    public void setUnreadMessagesCount(Long unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }
}
