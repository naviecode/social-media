package com.project.social_media.dto;

import java.time.LocalDateTime;

public class MessageWithSenderNameDto {
    private Long messageId;
    private Long chatId;
    private Long senderId;
    private String messageText;
    private LocalDateTime sentAt;
    private Boolean isRead;
    private String senderName;
    public String avatarUrl;


    public MessageWithSenderNameDto(Long messageId, Long chatId, Long senderId, String messageText, LocalDateTime sentAt, Boolean isRead, String senderName, String avatarUrl) {
        this.messageId = messageId;
        this.chatId = chatId;
        this.senderId = senderId;
        this.messageText = messageText;
        this.sentAt = sentAt;
        this.isRead = isRead;
        this.senderName = senderName;
        this.avatarUrl = avatarUrl;
    }
    // Getters and setters
    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
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

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
