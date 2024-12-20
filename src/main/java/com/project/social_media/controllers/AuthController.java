package com.project.social_media.controllers;

import com.project.social_media.Authorize.JwtUtils;
import com.project.social_media.dto.UserLogin;
import com.project.social_media.models.ResponseServiceEntity;
import com.project.social_media.models.Users;
import com.project.social_media.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        String token = getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            return "redirect:/";
        }
        model.addAttribute("user", new UserLogin());

        return "auth/login";
    }
    @GetMapping("/register")
    public String register(HttpServletRequest request, Model model){
        String token = getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            return "redirect:/";
        }
        model.addAttribute("user", new Users());
        return "auth/register";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") Users user,
                           BindingResult bindingResult,
                           Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Validation failed");
            return "auth/register";
        }
        ResponseServiceEntity<Users> result = authService.register(user);
        if(result.getErrorCode().equals("0")) {
            model.addAttribute("message", result.getMessage());
            return "redirect:/auth/login";
        }
        model.addAttribute("message", result.getMessage());
        return "auth/register";
    }
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("user") UserLogin user,
                        BindingResult bindingResult,
                        HttpServletResponse response,
                        Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Validation failed");
            return "auth/login";
        }
        ResponseServiceEntity<String> result = authService.login(user.getUsername(), user.getPassword());
        if(result.getErrorCode().equals("0")){
            String token = result.getData();
            Cookie  jwtCookie = new Cookie("token", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setMaxAge(24*60*60);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);
            return "redirect:/";
        }
        model.addAttribute("errors", "Tên đăng nhập hoặc mật khẩu không hợp lệ");
        return "auth/login";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("token", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
        return "redirect:/auth/login";
    }
    private String getJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
