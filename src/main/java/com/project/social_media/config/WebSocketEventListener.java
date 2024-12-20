//package com.project.social_media.config;
//
//import com.project.social_media.services.UserService;
//import com.project.social_media.utils.SecurityUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.messaging.SessionConnectEvent;
//import org.springframework.web.socket.messaging.SessionConnectedEvent;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//
//@Component
//public class WebSocketEventListener {
//    @Autowired
//    private UserService userService;
//
//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        Long userId = SecurityUtils.getLoggedInUserId();
//        userService.updateActivity(userId);
//    }
//
//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        Long userId = SecurityUtils.getLoggedInUserId();
//        userService.markUserInactive(userId);
//    }
//}
