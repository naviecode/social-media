package com.project.social_media.repository;

import com.project.social_media.dto.MessageWithSenderNameDto;
import com.project.social_media.models.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageRepository extends JpaRepository<Messages, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Messages m SET m.isRead = true WHERE m.chatId = :chatId")
    int markMessagesAsReadByChatId(@Param("chatId") Long chatId);

    @Modifying
    @Transactional
    @Query("UPDATE Messages m SET m.isRead = true WHERE m.chatId = :chatId AND m.senderId <> :senderId")
    int markMessagesAsReadByChatIdAndSenderId(@Param("chatId") Long chatId, @Param("senderId") Long senderId);

    List<Messages> findByChatId(Long chatId);

    @Query("SELECT new com.project.social_media.dto.MessageWithSenderNameDto(m.messageId, m.chatId, m.senderId, m.messageText, m.sentAt, m.isRead, u.fullName) " +
            "FROM Messages m JOIN Users u ON m.senderId = u.userId WHERE m.chatId = :chatId")
    List<MessageWithSenderNameDto> findMessagesWithSenderNameByChatId(@Param("chatId") Long chatId);



}
