package com.project.social_media.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_status")
public class UserStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "is_online", nullable = false)
    private Boolean isOnline;

    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt;

    // Constructors
    public UserStatus() {
    }

    public UserStatus(Boolean isOnline, LocalDateTime lastActiveAt) {
        this.isOnline = isOnline;
        this.lastActiveAt = lastActiveAt;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public LocalDateTime getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(LocalDateTime lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

}

