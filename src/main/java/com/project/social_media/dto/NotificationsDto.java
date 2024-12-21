package com.project.social_media.dto;

import java.time.LocalDateTime;

public class NotificationsDto {
    private Long chatId;
    private Long senderId;
    private String fullName;
    private String content;
    private String type;
    private String attr1;
    private String notifyType;
    private String avatarUrl;
    private LocalDateTime createdAt;

    public NotificationsDto(){

    }

    public NotificationsDto(Long senderId, String avatarUrl, String content, String fullName, String notifyType, LocalDateTime createdAt) {
        this.senderId = senderId;
        this.avatarUrl = avatarUrl;
        this.content = content;
        this.fullName = fullName;
        this.notifyType = notifyType;
        this.createdAt = createdAt;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAttr1() {
        return attr1;
    }
    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public String getNotifyType() {
        return notifyType;
    }
    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
