package com.project.social_media.services;

import com.project.social_media.constants.ErrorCodes;
import com.project.social_media.models.ResponseServiceEntity;
import com.project.social_media.models.ResponseServiceListEntity;
import com.project.social_media.models.Users;
import com.project.social_media.repository.ChatMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMemberService {

    @Autowired
    private ChatMemberRepository chatMemberRepository;
    public ResponseServiceEntity<Long> GetChatIdTwoUser(Long userId1, Long userId2){
        Long result = chatMemberRepository.findChatIdByUserIds(userId1, userId2);
        return ResponseServiceEntity.success(result, ErrorCodes.SUCCESS);
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
}
