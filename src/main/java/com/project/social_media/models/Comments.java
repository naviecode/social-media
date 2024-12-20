package com.project.social_media.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Comments")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Posts post;

    @ManyToOne
    @JoinColumn(name = "sharedPostId")
    private SharedPosts sharedPost;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "parentCommentId")
    private Comments parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<Comments> replies;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private Set<CommentReactions> reactions;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Comments() {
        this.createdAt = LocalDateTime.now();
    }

    public Comments(Posts post, SharedPosts sharedPost, Users user, Comments parentComment, String content) {
        this.post = post;
        this.sharedPost = sharedPost;
        this.user = user;
        this.parentComment = parentComment;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Posts getPost() {
        return post;
    }

    public void setPost(Posts post) {
        this.post = post;
    }

    public SharedPosts getSharedPost() {
        return sharedPost;
    }

    public void setSharedPost(SharedPosts sharedPost) {
        this.sharedPost = sharedPost;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Comments getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comments parentComment) {
        this.parentComment = parentComment;
    }

    public List<Comments> getReplies() {
        return replies;
    }

    public void setReplies(List<Comments> replies) {
        this.replies = replies;
    }

    public Set<CommentReactions> getReactions() {
        return reactions;
    }

    public void setReactions(Set<CommentReactions> reactions) {
        this.reactions = reactions;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}