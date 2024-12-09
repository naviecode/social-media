package com.project.social_media.controllers;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping("/")
    public String homePage(Model model) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("username", username);

        return "pages/trangchu";
    }
}
