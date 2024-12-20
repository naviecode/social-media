package com.project.social_media.repository;

import com.project.social_media.models.Comments;
import com.project.social_media.models.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    long countByPost(Posts post);
    long countByPost_PostId(Long postId);
    List<Comments> findByPost_PostId(Long postId);
}
