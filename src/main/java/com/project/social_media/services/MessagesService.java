package com.project.social_media.services;

import com.project.social_media.constants.ErrorCodes;
import com.project.social_media.dto.MessageWithSenderNameDto;
import com.project.social_media.dto.ResponseServiceEntity;
import com.project.social_media.dto.ResponseServiceListEntity;
import com.project.social_media.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessagesService {
    @Autowired
    private MessageRepository messageRepository;

    public ResponseServiceListEntity<MessageWithSenderNameDto> GetAllMessagesByChatId(Long chatId) {
        List<MessageWithSenderNameDto> result = messageRepository.findMessagesWithSenderNameByChatId(chatId);
        return ResponseServiceListEntity.success(result, result.stream().count(), ErrorCodes.SUCCESS);
    }


    public ResponseServiceEntity<Integer> markMessagesAsReadByChatId(Long chatId) {
        int updatedCount = messageRepository.markMessagesAsReadByChatId(chatId);
        return ResponseServiceEntity.success(updatedCount, ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<Integer> markMessagesAsReadByChatIdAndSenderId(Long chatId, Long senderId) {
        int updatedCount = messageRepository.markMessagesAsReadByChatIdAndSenderId(chatId, senderId);
        return ResponseServiceEntity.success(updatedCount, ErrorCodes.SUCCESS);
    }

}
