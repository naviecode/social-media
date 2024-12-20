package com.project.social_media.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Reactions")
public class Reactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Posts post;


    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    public Reactions() {
        this.createdAt = LocalDateTime.now();
    }

    public Reactions(Users user, Posts post) {
        this.user = user;
        this.post = post;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getReactionId() {
        return reactionId;
    }

    public void setReactionId(Long reactionId) {
        this.reactionId = reactionId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Posts getPost() {
        return post;
    }

    public void setPost(Posts post) {
        this.post = post;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}