package com.project.social_media.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    // ID của người nhận thông báo (dùng cho 1-1)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "sender_id")
    private Long senderId;

    // ID của nhóm (dùng cho thông báo nhóm)
    @Column(name = "group_id")
    private Long groupId;

    // Nội dung thông báo
    @Column(name = "content", nullable = false)
    private String content;

    // Loại thông báo: VD: "group", "personal", "system"
    @Column(name = "notification_type", nullable = false)
    private String notificationType;

    // Trạng thái đã đọc hay chưa
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    // Thời gian tạo thông báo
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Thời gian cập nhật cuối cùng (nếu cần)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Notifications() {}

    // Parameterized constructor
    public Notifications(Long userId, Long senderId, Long groupId, String content, String notificationType) {
        this.userId = userId;
        this.groupId = groupId;
        this.content = content;
        this.notificationType = notificationType;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
        this.senderId = senderId;
    }

    // Getters and Setters
    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getSenderId() {
        return senderId;
    }
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
}

