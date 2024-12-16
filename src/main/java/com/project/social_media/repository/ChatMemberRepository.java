package com.project.social_media.repository;

import com.project.social_media.models.ChatMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatMemberRepository extends JpaRepository<ChatMembers, Long> {
    boolean existsByChatIdAndUserId(Long chatId, Long userId);

    @Query("SELECT cm1.chatId " +
            "FROM ChatMembers cm1, ChatMembers cm2 " +
            "WHERE cm1.chatId = cm2.chatId " +
            "AND cm1.userId = :userId1 " +
            "AND cm2.userId = :userId2")
    Long findChatIdByUserIds(Long userId1, Long userId2);
}
