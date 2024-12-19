package com.project.social_media.repository;

import com.project.social_media.dto.ChatGroupWithUnreadCountDto;
import com.project.social_media.models.Chats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chats, Long> {
    @Query("SELECT new com.project.social_media.dto.ChatGroupWithUnreadCountDto(c.chatId, c.groupName, COUNT(m.messageId)) " +
            "FROM Chats c JOIN ChatMembers cm ON c.chatId = cm.chatId " +
            "LEFT JOIN Messages m ON c.chatId = m.chatId AND m.isRead = false AND m.senderId <> :userId " +
            "WHERE cm.userId = :userId AND c.isGroupChat = true " +
            "GROUP BY c.chatId " +
            "ORDER BY MAX(m.sentAt) DESC")
    List<ChatGroupWithUnreadCountDto> findChatGroupsByUserIdWithUnreadCount(@Param("userId") Long userId);
}
