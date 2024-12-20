package com.project.social_media.controllers;

import com.project.social_media.dto.ChatGroupWithUnreadCountDto;
import com.project.social_media.dto.FriendWithUsernameDto;
import com.project.social_media.dto.UserInfoDto;
import com.project.social_media.models.Users;
import com.project.social_media.services.FriendService;
import com.project.social_media.services.NotificationsService;
import com.project.social_media.services.UserService;
import com.project.social_media.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private NotificationsService notificationsService;

    @RequestMapping("/{userId}")
    public String getUserInfo(@PathVariable("userId") Long userId, Model model) {
        UserInfoDto user = userService.getUserInfo(userId).getData();
        Long userLogin = SecurityUtils.getLoggedInUserId();

//        model.addAttribute("areFriends", friendService.areFriends(userLogin, userId).getData());
//        String statusFriend = friendService.getFriendStatus(userId, userLogin).getData();
//        String existStatus = friendService.getFriendStatus(userLogin, userId).getData();
//        model.addAttribute("statusFriend", statusFriend);
//        model.addAttribute("existStatus", existStatus);
        model.addAttribute("userLogin", userLogin);
//        model.addAttribute("userId", userId);
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("notifications", notificationsService.getNotificationsByUserId(userLogin));

        if (user != null) {
            model.addAttribute("user", user);
            return "user/user";
        } else {
            model.addAttribute("error", "User not found");
            return "user/error";
        }
    }

    @GetMapping("/PartialUserProfileStatus")
    public String PartialChatListFriend(@RequestParam("userId") Long userId, Model model){
        UserInfoDto user = userService.getUserInfo(userId).getData();
        Long userLogin = SecurityUtils.getLoggedInUserId();
        model.addAttribute("areFriends", friendService.areFriends(userLogin, userId).getData());
        String statusFriend = friendService.getFriendStatus(userId, userLogin).getData();
        String existStatus = friendService.getFriendStatus(userLogin, userId).getData();
        model.addAttribute("statusFriend", statusFriend);
        model.addAttribute("existStatus", existStatus);
        model.addAttribute("userLogin", userLogin);
        model.addAttribute("userId", userId);
        model.addAttribute("user", user);


        return "partial/user_profile_status";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Users> searchUsersByName(@RequestParam("name") String name) {
        Long userLogin = SecurityUtils.getLoggedInUserId();
        return userService.searchUsersByName(name,userLogin);
    }

}
