package com.project.social_media.repository;

import com.project.social_media.models.CommentReactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReactionsRepository extends JpaRepository<CommentReactions, Long> {
}
