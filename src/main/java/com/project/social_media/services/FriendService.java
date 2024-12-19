package com.project.social_media.services;

import com.project.social_media.constants.ErrorCodes;
import com.project.social_media.dto.FriendWithUsernameDto;
import com.project.social_media.models.Friends;
import com.project.social_media.models.ResponseServiceListEntity;
import com.project.social_media.repository.FriendsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public ResponseServiceListEntity<FriendWithUsernameDto> searchFriends(Long userId, String name){
        List<FriendWithUsernameDto> result = friendsRepository.findFriendsWithUsernameByUserId1AndName(userId, name);
        return ResponseServiceListEntity.success(result, result.stream().count(), ErrorCodes.SUCCESS);
    }
}
