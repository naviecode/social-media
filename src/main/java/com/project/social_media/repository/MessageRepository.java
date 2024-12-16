package com.project.social_media.repository;

import com.project.social_media.models.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Messages, Long> {
    List<Messages> findByChatId(Long chatId);
}
