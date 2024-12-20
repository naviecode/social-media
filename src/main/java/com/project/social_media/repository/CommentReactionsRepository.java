package com.project.social_media.repository;

import com.project.social_media.models.CommentReactions;
import com.project.social_media.models.Comments;
import com.project.social_media.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReactionsRepository extends JpaRepository<CommentReactions, Long> {
    void deleteByCommentAndUser(Comments comment, Users user);
}
