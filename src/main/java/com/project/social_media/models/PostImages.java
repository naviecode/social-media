package com.project.social_media.models;

import jakarta.persistence.*;

@Entity
@Table(name = "PostImages")
public class PostImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Posts post;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB", nullable = false)
    private byte[] imageData;

    public PostImages() {
    }

    public PostImages(Posts post, byte[] imageData) {
        this.post = post;
        this.imageData = imageData;
    }

    // Getters and Setters

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Posts getPost() {
        return post;
    }

    public void setPost(Posts post) {
        this.post = post;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}