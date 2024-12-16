package com.project.social_media.services;

import com.project.social_media.constants.ErrorCodes;
import com.project.social_media.models.Messages;
import com.project.social_media.models.ResponseServiceListEntity;
import com.project.social_media.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessagesService {
    @Autowired
    private MessageRepository messageRepository;

    public ResponseServiceListEntity<Messages> GetAllMessagesByChatId(Long chatId) {
        List<Messages> result = messageRepository.findByChatId(chatId);
        return ResponseServiceListEntity.success(result, result.stream().count(), ErrorCodes.SUCCESS);
    }


}
