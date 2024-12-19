package com.project.social_media.patterns.Observer;

import com.project.social_media.dto.NotificationsDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class FriendObserver implements UserObserver {
    private String username;
    private SimpMessagingTemplate messagingTemplate;

    public FriendObserver(String username, SimpMessagingTemplate messagingTemplate) {
        this.username = username;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void update(NotificationsDto notify) {
        // Gửi thông báo đến user qua WebSocket
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", notify);
    }
}
