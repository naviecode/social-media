package com.project.social_media.repository;

import com.project.social_media.models.PostImages;
import com.project.social_media.models.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImagesRepository extends JpaRepository<PostImages, Long> {
    List<PostImages> findByPost(Posts post);
}
