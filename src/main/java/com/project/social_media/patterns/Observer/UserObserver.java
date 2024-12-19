package com.project.social_media.patterns.Observer;

import com.project.social_media.dto.NotificationsDto;
import com.project.social_media.models.ChatMessage;

public interface UserObserver {
    void update(NotificationsDto notify);
}
