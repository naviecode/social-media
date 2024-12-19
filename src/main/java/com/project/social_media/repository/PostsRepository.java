package com.project.social_media.repository;

import com.project.social_media.models.Posts;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Query("SELECT p FROM Posts p WHERE p.user.userId = :userId OR p.user.userId IN :friendIds ORDER BY p.createdAt DESC")
    Page<Posts> findPostsByUserIdOrFriendIds(Long userId, List<Long> friendIds, Pageable pageable);
}
