package com.project.social_media.services;

import com.project.social_media.constants.ErrorCodes;
import com.project.social_media.models.ChatMembers;
import com.project.social_media.dto.ResponseServiceEntity;
import com.project.social_media.dto.ResponseServiceListEntity;
import com.project.social_media.models.Users;
import com.project.social_media.repository.ChatMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMemberService {

    @Autowired
    private ChatMemberRepository chatMemberRepository;
    public ResponseServiceEntity<Long> GetChatIdTwoUser(Long userId1, Long userId2){
        List<Long> chatIds = chatMemberRepository.findChatIdsByUserIds(userId1, userId2);
        Long chatId = chatIds.isEmpty() ? null : chatIds.get(0);
        return ResponseServiceEntity.success(chatId, ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<Boolean> isMemberOfChat(Long userId, Long chatId) {
        boolean isMember = chatMemberRepository.isMemberOfChat(chatId, userId);
        return ResponseServiceEntity.success(isMember, ErrorCodes.SUCCESS);
    }

    public ResponseServiceListEntity<Users> getUsersByChatId(Long chatId, Long userIdLogin) {
        List<Users> users = chatMemberRepository.findUsersByChatId(chatId, userIdLogin);
        return ResponseServiceListEntity.success(users, users.stream().count(), ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<Long> countMembersByChatId(Long chatId) {
        return ResponseServiceEntity.success(chatMemberRepository.countMembersByChatId(chatId), ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<Long> insertChatMember(Long chatId, Long userId) {
        ChatMembers newChatMember = new ChatMembers(chatId, userId, LocalDateTime.now());
        ChatMembers savedChatMember = chatMemberRepository.save(newChatMember);
        return ResponseServiceEntity.success(savedChatMember.getMemberId(), ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<Void> deleteChatMembersByChatId(Long chatId) {
        chatMemberRepository.deleteByChatId(chatId);
        return ResponseServiceEntity.success(null, ErrorCodes.SUCCESS);
    }
}
