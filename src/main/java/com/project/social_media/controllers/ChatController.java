package com.project.social_media.controllers;


import com.project.social_media.models.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @GetMapping("/")
    public String chat(){
        return "chat/chat";
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        // Logic lưu tin nhắn vào database
        saveMessageToDatabase(message);
        return message;
    }

    private void saveMessageToDatabase(ChatMessage message) {
        // Kết nối và lưu vào database
//        String url = "jdbc:mysql://localhost:3306/chatdb";
//        String user = "root";
//        String password = "password";
//
//        try (Connection connection = DriverManager.getConnection(url, user, password)) {
//            String query = "INSERT INTO messages (username, message) VALUES (?, ?)";
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1, message.getUsername());
//            preparedStatement.setString(2, message.getMessage());
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
