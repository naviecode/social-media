package com.project.social_media.repository;

import com.project.social_media.models.Chats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chats, Long> {
    @Query("SELECT c FROM Chats c JOIN ChatMembers cm ON c.chatId = cm.chatId WHERE cm.userId = :userId AND c.isGroupChat = true")
    List<Chats> findChatGroupsByUserId(@Param("userId") Long userId);
}
