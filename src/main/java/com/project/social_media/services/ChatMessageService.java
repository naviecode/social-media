package com.project.social_media.services;

import com.project.social_media.constants.ErrorCodes;
import com.project.social_media.dto.ChatMessage;
import com.project.social_media.models.Messages;
import com.project.social_media.dto.ResponseServiceEntity;
import com.project.social_media.repository.ChatMemberRepository;
import com.project.social_media.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatMessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatMemberRepository chatMemberRepository;

    public ResponseServiceEntity<ChatMessage> saveMessages(ChatMessage chatMessage) {
        boolean isMember = chatMemberRepository.existsByChatIdAndUserId(chatMessage.getChatId(), chatMessage.getSenderId());
        if (!isMember) {
            return ResponseServiceEntity.error(ErrorCodes.ERROR_CHAT_MESSAGE_EXISTS);
        }

        Messages message = new Messages();
        message.setChatId(chatMessage.getChatId());
        message.setSenderId(chatMessage.getSenderId());
        message.setMessageText(chatMessage.getContent());
        message.setSentAt(LocalDateTime.now());
        message.setIsRead(false);

        messageRepository.save(message);

        return ResponseServiceEntity.success(chatMessage, ErrorCodes.SUCCESS);

    }

//    public void saveMessage(ChatMessage chatMessage) {
//        // Kiểm tra sender có thuộc cuộc trò chuyện không
//        boolean isMember = chatMemberRepository.existsByChatIdAndUserId(chatMessage.getChatId(), chatMessage.getSenderId());
//        if (!isMember) {
//            throw new IllegalArgumentException("User is not a member of the chat!");
//        }
//
//        // Lưu tin nhắn vào database
//        Messages message = new Messages();
//        message.setChatId(chatMessage.getChatId());
//        message.setSenderId(chatMessage.getSenderId());
//        message.setMessageText(chatMessage.getContent());
//        message.setSentAt(LocalDateTime.now());
//        message.setIsRead(false);
//
//        messageRepository.save(message);
//    }
}
