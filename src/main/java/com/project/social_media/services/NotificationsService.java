package com.project.social_media.services;

import com.project.social_media.dto.NotificationsDto;
import com.project.social_media.models.Notifications;
import com.project.social_media.repository.NotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationsService {
    @Autowired
    private NotificationsRepository notificationsRepository;

    public Notifications insertNotification(Notifications notification) {
        return notificationsRepository.save(notification);
    }

    public void updateNotificationStatus(Long notificationId, Boolean isRead) {
        notificationsRepository.updateNotificationStatus(notificationId, isRead);
    }

    public List<NotificationsDto> getNotificationsByUserId(Long userId) {
        return notificationsRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    public void updateNotificationStatusByUserId(Long userId, Boolean isRead) {
        notificationsRepository.updateNotificationStatusByUserId(userId, isRead);
    }
}
