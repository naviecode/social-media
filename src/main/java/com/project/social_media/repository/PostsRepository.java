package com.project.social_media.repository;

import com.project.social_media.models.Posts;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Query("SELECT p FROM Posts p WHERE (p.user.userId = :userId OR p.user.userId IN :friendIds) AND (p.privacy <> 'ONLY_ME' OR p.user.userId = :userId)")
    Page<Posts> findPostsByUserIdOrFriendIds(Long userId, List<Long> friendIds, Pageable pageable);
    Page<Posts> findAllByUser_UserId(Long userId, Pageable pageable);
    Page<Posts> findByUser_UserIdAndPrivacyIn(Long userId, List<Posts.Privacy> privacies, Pageable pageable);
    Page<Posts> findByUser_UserIdAndPrivacy(Long userId, Posts.Privacy privacy, Pageable pageable);
    @Query("SELECT COUNT(p) FROM Posts p WHERE p.user.userId = :userId")
    long countPostsByUserId(@Param("userId") Long userId);
}
