package com.project.social_media.utils;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    public static Long getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object details = authentication.getCredentials();
            if (details instanceof Long) {
                return (Long) details;
            } else if (details instanceof String) {
                return Long.valueOf((String) details);
            }
        }
        return null;
    }
}
