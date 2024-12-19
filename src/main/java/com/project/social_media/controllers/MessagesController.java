package com.project.social_media.controllers;

import com.project.social_media.models.ResponseServiceEntity;
import com.project.social_media.services.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessagesController {
    @Autowired
    private MessagesService messagesService;

    @PostMapping("/updateIsRead")
    public ResponseServiceEntity<Integer> updateIsRead(@RequestParam Long chatId, @RequestParam Long senderId) {
        return messagesService.markMessagesAsReadByChatIdAndSenderId(chatId, senderId);
    }
}
