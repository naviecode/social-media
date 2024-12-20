package com.project.social_media.patterns.Observer;

import com.project.social_media.dto.NotificationsDto;

public interface UserObserver {
    void update(NotificationsDto notify);
}
