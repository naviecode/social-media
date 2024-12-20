package com.project.social_media.controllers;

import com.project.social_media.models.Users;
import com.project.social_media.services.NotificationsService;
import com.project.social_media.services.UserService;
import com.project.social_media.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationsService notificationsService;

    @GetMapping("/")
    public String homePage(Model model) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = SecurityUtils.getLoggedInUserId();
        Users user = userService.getUserById(userId).getData();
        model.addAttribute("username", username);
        model.addAttribute("userLogin", userId);
        model.addAttribute("notifications", notificationsService.getNotificationsByUserId(userId));
        model.addAttribute("userLoginFullName", user.getFullName());
        model.addAttribute("userLoginAvatarUrl", user.getAvatarURL());
        return "pages/home";
    }
}
