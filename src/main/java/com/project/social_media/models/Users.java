package com.project.social_media.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name="Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username must not exceed 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 1, message = "Password must be at least 1 characters")
    @Column(nullable = false)
    private String password;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(nullable = true, length = 100)
    private String email;

    @NotBlank(message = "FullName is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    @Column(nullable = true, length = 100)
    private String fullName;
    @Column(nullable = true, length = 1000)
    private String avatarURL;


    @Column(nullable = true)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime lastActive;

    @Column(nullable = true, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isActive;

    public Users() {
        this.createdAt = LocalDateTime.now();
    }

    public Users(Long userId, String fullName) {
        this.userId = userId;
        this.fullName = fullName;
    }

    public Users(String username, String password, String email, String fullName, String avatarURL) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.avatarURL = avatarURL;
        this.createdAt = LocalDateTime.now();
        this.isActive = false;
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

    public LocalDateTime getLastActive() {
        return lastActive;
    }

    public void setLastActive(LocalDateTime lastActive) {
        this.lastActive = lastActive;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
