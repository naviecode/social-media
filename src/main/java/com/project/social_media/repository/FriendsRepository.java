package com.project.social_media.repository;

import com.project.social_media.dto.FriendWithUsernameDto;
import com.project.social_media.models.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendsRepository  extends JpaRepository<Friends, Long> {

    List<Friends> findByUserId1(Long userId1);

    @Query("SELECT new com.project.social_media.dto.FriendWithUsernameDto(f.friendId, f.userId1, f.userId2, f.status, u.fullName) " +
            "FROM Friends f JOIN Users u ON f.userId2 = u.userId " +
            "WHERE f.userId1 = :userId1 AND LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<FriendWithUsernameDto> findFriendsWithUsernameByUserId1AndName(
            @Param("userId1") Long userId1,
            @Param("name") String name
    );
    @Query("SELECT new com.project.social_media.dto.FriendWithUsernameDto(f.friendId, f.userId1, f.userId2, f.status, u.fullName, cm.chatId) " +
            "FROM Friends f " +
            "JOIN Users u ON f.userId2 = u.userId " +
            "JOIN ChatMembers cm ON cm.userId = f.userId2 AND cm.chatId IN " +
            "(SELECT cm2.chatId FROM ChatMembers cm2 WHERE cm2.userId = :userId1) " +
            "WHERE f.userId1 = :userId1 AND " +
            "(SELECT COUNT(cm3.userId) FROM ChatMembers cm3 WHERE cm3.chatId = cm.chatId) = 2")
    List<FriendWithUsernameDto> findFriendsWithUsernameByUserId1(@Param("userId1") Long userId1);

}
