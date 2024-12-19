package com.project.social_media.repository;

import com.project.social_media.models.ChatMembers;
import com.project.social_media.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMemberRepository extends JpaRepository<ChatMembers, Long> {
    boolean existsByChatIdAndUserId(Long chatId, Long userId);

    @Query("SELECT cm1.chatId " +
            "FROM ChatMembers cm1, ChatMembers cm2 " +
            "WHERE cm1.chatId = cm2.chatId " +
            "AND cm1.userId = :userId1 " +
            "AND cm2.userId = :userId2")
    Long findChatIdByUserIds(Long userId1, Long userId2);

    @Query("SELECT COUNT(cm) > 0 FROM ChatMembers cm WHERE cm.chatId = :chatId AND cm.userId = :userId")
    boolean isMemberOfChat(@Param("chatId") Long chatId, @Param("userId") Long userId);

    @Query("SELECT cm FROM ChatMembers cm WHERE cm.chatId = :chatId AND cm.userId = :userId")
    ChatMembers findByChatIdAndUserId(@Param("chatId") Long chatId, @Param("userId") Long userId);


    @Query("SELECT new com.project.social_media.models.Users(u.userId, u.fullName) " +
            "FROM ChatMembers cm JOIN Users u ON cm.userId = u.userId " +
            "WHERE cm.chatId = :chatId AND cm.userId <> :loggedInUserId")
    List<Users> findUsersByChatId(@Param("chatId") Long chatId, @Param("loggedInUserId") Long loggedInUserId);

    @Query("SELECT COUNT(cm) FROM ChatMembers cm WHERE cm.chatId = :chatId")
    long countMembersByChatId(@Param("chatId") Long chatId);
}
