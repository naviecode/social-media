package com.project.social_media.controllers;

import com.project.social_media.dto.ChatGroupWithUnreadCountDto;
import com.project.social_media.dto.FriendWithUsernameDto;
import com.project.social_media.dto.UserInfoDto;
import com.project.social_media.models.Users;
import com.project.social_media.services.*;
import com.project.social_media.utils.SecurityUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Value("${file.upload-dir}")
    private  String UploadDir;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private PostService postService;

    @Autowired
    private NotificationsService notificationsService;

    @RequestMapping("/{userId}")
    public String getUserInfo(@PathVariable("userId") Long userId, Model model) {
        UserInfoDto user = userService.getUserInfo(userId).getData();
        Long userLogin = SecurityUtils.getLoggedInUserId();
        model.addAttribute("userLogin", userLogin);
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("notifications", notificationsService.getNotificationsByUserId(userLogin));
        model.addAttribute("chatGroups", chatService.getChatGroupsByUserId(userId).getData());
        model.addAttribute("postCount", postService.countPostsByUserId(userId));

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
        model.addAttribute("postCount", postService.countPostsByUserId(userId));


        return "partial/user_profile_status";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Users> searchUsersByName(@RequestParam("name") String name) {
        Long userLogin = SecurityUtils.getLoggedInUserId();
        return userService.searchUsersByName(name,userLogin);
    }

    @GetMapping("/user/status/{userId}")
    public String getUserStatus(@PathVariable Long userId) {
        return userService.getUserStatus(userId);
    }
    
    @GetMapping("/update-activity")
    public String updateActivity(HttpSession session) {
        Long userId = SecurityUtils.getLoggedInUserId();
        userService.updateActivity(userId);
        return "User activity updated";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile file){
        if(file.isEmpty()){
            return "Chọn file";
        }
        try{
            Path path = Paths.get(UploadDir + File.separator + file.getOriginalFilename());
            Files.copy(file.getInputStream(), path);
            return "redirect:/user/" + SecurityUtils.getLoggedInUserId();
        }catch (IOException e){
            e.printStackTrace();
            return "redirect:/home";
        }

    }

}
