package com.project.social_media.repository;

import com.project.social_media.models.Posts;
import com.project.social_media.models.Reactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ReactionsRepository extends JpaRepository<Reactions, Long> {
    long countByPost(Posts post);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reactions r WHERE r.post = :post AND r.user.userId = :userId")
    boolean existsByPostAndUserId(@Param("post") Posts post, @Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Reactions r WHERE r.post = :post AND r.user.userId = :userId")
    void deleteByPostAndUserId(@Param("post") Posts post, @Param("userId") Long userId);
}