package com.project.social_media.services;

import com.project.social_media.constants.ErrorCodes;
import com.project.social_media.dto.FriendWithUsernameDto;
import com.project.social_media.models.Friends;
import com.project.social_media.dto.ResponseServiceEntity;
import com.project.social_media.dto.ResponseServiceListEntity;
import com.project.social_media.repository.FriendsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FriendService {
    @Autowired
    private FriendsRepository friendsRepository;

    public ResponseServiceListEntity<FriendWithUsernameDto> getFriendsByUserId(Long userId) {
        List<FriendWithUsernameDto> result =  friendsRepository.findByUserId1(userId);
        return ResponseServiceListEntity.success(result, result.stream().count(), ErrorCodes.SUCCESS);
    }

    public ResponseServiceListEntity<FriendWithUsernameDto> getFriendsWithUsernameByUserId1(Long userId, String name) {
        List<FriendWithUsernameDto> result = friendsRepository.findFriendsWithUsernameByUserId1(userId, name);
        return ResponseServiceListEntity.success(result, result.stream().count(), ErrorCodes.SUCCESS);
    }

    public ResponseServiceListEntity<FriendWithUsernameDto> getFindNonFriendsWithUsernameByUserId1(Long userId, String name) {
        List<FriendWithUsernameDto> result = friendsRepository.findNonFriendsWithUsernameByUserId1(userId, name);
        return ResponseServiceListEntity.success(result, result.stream().count(), ErrorCodes.SUCCESS);
    }

    public ResponseServiceListEntity<FriendWithUsernameDto> searchFriends(Long userId, String name){
        List<FriendWithUsernameDto> result = friendsRepository.findFriendsWithUsernameByUserId1AndName(userId, name);
        return ResponseServiceListEntity.success(result, result.stream().count(), ErrorCodes.SUCCESS);
    }

    public ResponseServiceListEntity<FriendWithUsernameDto> getFriendByUserId(Long userId){
        List<FriendWithUsernameDto> result = friendsRepository.getFriendByUserID(userId);
        return ResponseServiceListEntity.success(result, result.stream().count(), ErrorCodes.SUCCESS);
    }
    public ResponseServiceEntity<Boolean> areFriends(Long userIdLogin, Long userId) {
        return ResponseServiceEntity.success(friendsRepository.isFriend(userIdLogin, userId), ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<Boolean> addFriend(Long userIdLogin, Long userId) {
        Friends friendRequest = new Friends();
        friendRequest.setUserId1(userIdLogin);
        friendRequest.setUserId2(userId);
        friendRequest.setStatus("pending");
        friendRequest.setCreatedAt(LocalDateTime.now());
        friendsRepository.save(friendRequest);
        return ResponseServiceEntity.success(true, ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<Boolean> hasPendingFriendRequest(Long userId1, Long userId2) {
        boolean exists = friendsRepository.existsPendingFriendRequest(userId1, userId2);
        return ResponseServiceEntity.success(exists, ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<Boolean> updateFriendStatus(Long userId1, Long userId2, String status) {
        friendsRepository.updateFriendStatus(userId1, userId2, status);
        return ResponseServiceEntity.success(true, ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<String> getFriendStatus(Long userIdLogin, Long userId) {
        String status = friendsRepository.findFriendStatus(userIdLogin, userId);
        return ResponseServiceEntity.success(status, ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<Boolean> deleteFriend(Long userId1, Long userId2) {
        friendsRepository.deleteFriendByUserIds(userId1, userId2);
        return ResponseServiceEntity.success(true, ErrorCodes.SUCCESS);
    }
}
