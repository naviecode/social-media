package com.project.social_media.controllers;

import com.project.social_media.dto.FriendWithUsernameDto;
import com.project.social_media.dto.ResponseServiceListEntity;
import com.project.social_media.services.FriendService;
import com.project.social_media.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/friends")
public class FriendsController {
    @Autowired
    private FriendService friendService;

    @GetMapping("/search")
    @ResponseBody
    public ResponseServiceListEntity<FriendWithUsernameDto> searchFriends(@RequestParam("name") String name) {
        Long userId = SecurityUtils.getLoggedInUserId();
        ResponseServiceListEntity<FriendWithUsernameDto> responseServiceListEntity = friendService.searchFriends(userId, name);
        return responseServiceListEntity;
    }
}
