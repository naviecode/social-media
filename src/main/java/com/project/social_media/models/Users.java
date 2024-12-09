package com.project.social_media.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(nullable = false)
    private String password;

    @Column(nullable = true, unique = true, length = 100)
    private String email;

    @Column(nullable = true, length = 100)
    private String fullName;

    private String avatarURL;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    public Users() {
        this.createdAt = LocalDateTime.now();
    }
    public Users(String username, String password, String email, String fullName, String avatarURL) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.avatarURL = avatarURL;
        this.createdAt = LocalDateTime.now();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
