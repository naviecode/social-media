package com.project.social_media.services;

import com.project.social_media.constants.ErrorCodes;
import com.project.social_media.models.ResponseServiceEntity;
import com.project.social_media.repository.ChatMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatMemberService {

    @Autowired
    private ChatMemberRepository chatMemberRepository;
    public ResponseServiceEntity<Long> GetChatIdTwoUser(Long userId1, Long userId2){
        Long result = chatMemberRepository.findChatIdByUserIds(userId1, userId2);
        return ResponseServiceEntity.success(result, ErrorCodes.SUCCESS);
    }
}
