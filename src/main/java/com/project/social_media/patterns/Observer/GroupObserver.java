package com.project.social_media.patterns.Observer;

import com.project.social_media.dto.NotificationsDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class GroupObserver implements UserObserver{
    private Long groupId;
    private SimpMessagingTemplate messagingTemplate;

    public GroupObserver(Long groupId, SimpMessagingTemplate messagingTemplate) {
        this.groupId = groupId;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void update(NotificationsDto notify) {
        messagingTemplate.convertAndSend("/topic/groups/" + groupId, notify);
    }
}
