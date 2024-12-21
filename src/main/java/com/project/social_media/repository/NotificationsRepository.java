package com.project.social_media.repository;

import com.project.social_media.dto.NotificationsDto;
import com.project.social_media.models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Notifications n SET n.isRead = :isRead, n.updatedAt = CURRENT_TIMESTAMP WHERE n.notificationId = :notificationId")
    void updateNotificationStatus(@Param("notificationId") Long notificationId, @Param("isRead") Boolean isRead);

//    @Query("SELECT n FROM Notifications n WHERE n.userId = :userId ORDER BY n.createdAt DESC")
//    List<NotificationsDto> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @Query("SELECT new  com.project.social_media.dto.NotificationsDto(n.senderId, u.avatarURL, n.content, u.fullName, n.notificationType, u.createdAt)" +
            "FROM Notifications n " +
            "LEFT JOIN Users u ON u.userId = n.senderId " +
            "WHERE n.userId = :userId " +
            "ORDER BY n.createdAt DESC")
    List<NotificationsDto> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Notifications n SET n.isRead = :isRead, n.updatedAt = CURRENT_TIMESTAMP WHERE n.userId = :userId")
    void updateNotificationStatusByUserId(@Param("userId") Long userId, @Param("isRead") Boolean isRead);
}
