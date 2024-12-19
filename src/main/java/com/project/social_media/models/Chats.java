package com.project.social_media.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chats")
public class Chats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "is_group_chat", nullable = false)
    private Boolean isGroupChat;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "user_id_created")
    private Long userIdCreated;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Chats() {
    }

    public Chats(Boolean isGroupChat, String groupName, LocalDateTime createdAt) {
        this.isGroupChat = isGroupChat;
        this.groupName = groupName;
        this.createdAt = createdAt;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Boolean getIsGroupChat() {
        return isGroupChat;
    }

    public void setIsGroupChat(Boolean isGroupChat) {
        this.isGroupChat = isGroupChat;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}