package com.project.social_media.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/popups")
public class PopupController {
    @GetMapping("/posting")
    public String getPosting() {
        return "popups/posting"; // This resolves to templates/popups/posting.html
    }
}
