package com.project.social_media.repository;

import com.project.social_media.dto.FriendWithUsernameDto;
import com.project.social_media.models.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FriendsRepository  extends JpaRepository<Friends, Long> {

    @Query("SELECT new com.project.social_media.dto.FriendWithUsernameDto(f.friendId, f.userId1, f.userId2, f.status, cm.chatId) " +
       "FROM Friends f " +
       "JOIN Users u ON f.userId2 = u.userId " +
       "JOIN ChatMembers cm ON cm.userId = f.userId2 AND cm.chatId IN " +
       "(SELECT cm2.chatId FROM ChatMembers cm2 WHERE cm2.userId = :userId1) " +
       "WHERE f.userId1 = :userId1")
    List<FriendWithUsernameDto> findByUserId1(@Param("userId1") Long userId1);

    @Query("SELECT new com.project.social_media.dto.FriendWithUsernameDto(f.friendId, f.userId1, f.userId2, f.status, u.fullName) " +
            "FROM Friends f JOIN Users u ON f.userId2 = u.userId " +
            "WHERE f.userId1 = :userId1 AND LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<FriendWithUsernameDto> findFriendsWithUsernameByUserId1AndName(
            @Param("userId1") Long userId1,
            @Param("name") String name
    );

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
            "FROM Friends f " +
            "WHERE ((f.userId1 = :userId1 AND f.userId2 = :userId2) " +
            "OR (f.userId1 = :userId2 AND f.userId2 = :userId1)) " +
            "AND f.status = 'accepted'")
    boolean isFriend(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    @Query("SELECT new com.project.social_media.dto.FriendWithUsernameDto(f.friendId, f.userId1, f.userId2, f.status, u.fullName, cm.chatId, u.username, " +
        "(SELECT COUNT(m) FROM Messages m WHERE m.chatId = cm.chatId AND m.isRead = false AND m.senderId <> :userId1), " +
        "(SELECT u2.username FROM Messages m2 JOIN Users u2 ON m2.senderId = u2.userId WHERE m2.chatId = cm.chatId ORDER BY m2.sentAt DESC LIMIT 1)) " +
        "FROM Friends f " +
        "JOIN Users u ON f.userId2 = u.userId " +
        "JOIN ChatMembers cm ON cm.userId = f.userId2 AND cm.chatId IN " +
        "(SELECT cm2.chatId FROM ChatMembers cm2 WHERE cm2.userId = :userId1) " +
        "WHERE f.userId1 = :userId1 AND f.status = 'accepted' AND " +
        "(SELECT COUNT(cm3.userId) FROM ChatMembers cm3 WHERE cm3.chatId = cm.chatId) = 2 AND " +
        "(LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%')) OR :name IS NULL OR :name = '') " +
        "ORDER BY (SELECT m.sentAt FROM Messages m WHERE m.chatId = cm.chatId ORDER BY m.sentAt DESC LIMIT 1) DESC")
    List<FriendWithUsernameDto> findFriendsWithUsernameByUserId1(@Param("userId1") Long userId1, @Param("name") String name);

    @Query("SELECT COUNT(f) " +
            "FROM Friends f " +
            "WHERE f.status = 'accepted' AND " +
            "(f.userId1 = :userId)")
    long countByUserId(@Param("userId") Long userId);


    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END FROM Friends f WHERE f.userId1 = :userId1 AND f.userId2 = :userId2 AND f.status = 'pending'")
    boolean existsPendingFriendRequest(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    @Modifying
    @Transactional
    @Query("UPDATE Friends f SET f.status = :status WHERE f.userId1 = :userId1 AND f.userId2 = :userId2 AND f.status = 'pending'")
    void updateFriendStatus(@Param("userId1") Long userId1, @Param("userId2") Long userId2, @Param("status") String status);

    @Query("SELECT f.status FROM Friends f WHERE (f.userId1 = :userIdLogin AND f.userId2 = :userId AND f.status != 'deny')")
    String findFriendStatus(@Param("userIdLogin") Long userIdLogin, @Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Friends f WHERE (f.userId1 = :userId1 AND f.userId2 = :userId2) OR (f.userId1 = :userId2 AND f.userId2 = :userId1)")
    void deleteFriendByUserIds(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

}
