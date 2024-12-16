package com.project.social_media.services;

import com.project.social_media.constants.ErrorCodes;
import com.project.social_media.models.ChatMembers;
import com.project.social_media.models.Chats;
import com.project.social_media.models.ResponseServiceEntity;
import com.project.social_media.repository.ChatMemberRepository;
import com.project.social_media.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMemberRepository chatMembersRepository;

    public ResponseServiceEntity<Long> createGroupChat(List<Long> userIds) {
        if (userIds.size() < 3) {
            return ResponseServiceEntity.error(ErrorCodes.ERROR_CHAT_ADD_GROUP_SIZE);
        }
        Chats newChat = new Chats();
        newChat.setIsGroupChat(true);
        newChat.setCreatedAt(LocalDateTime.now());
        Chats savedChat = chatRepository.save(newChat);

        for (Long userId : userIds) {
            ChatMembers chatMember = new ChatMembers(savedChat.getChatId(), userId, LocalDateTime.now());
            chatMembersRepository.save(chatMember);
        }

        return ResponseServiceEntity.success(savedChat.getChatId(), ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<List<Chats>> getChatGroupsByUserId(Long userId) {
        List<Chats> chatGroups = chatRepository.findChatGroupsByUserId(userId);
        return ResponseServiceEntity.success(chatGroups, ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<Chats> getChatById(Long chatId) {
        Chats chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            return ResponseServiceEntity.error(ErrorCodes.ERROR_CHAT_NOT_FOUND);
        }
        return ResponseServiceEntity.success(chat, ErrorCodes.SUCCESS);
    }
}
