package com.project.social_media.controllers;


import com.project.social_media.Authorize.JwtUtils;
import com.project.social_media.constants.ErrorCodes;
import com.project.social_media.dto.FriendWithUsernameDto;
import com.project.social_media.models.*;
import com.project.social_media.services.*;
import com.project.social_media.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatMessageService chatMesageService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private ChatMemberService chatMemberService;

    @Autowired
    private MessagesService messagesService;

    @Autowired
    private ChatService chatService;

    @GetMapping
    public String chat(Model model)
    {
        return "chat/chatview";
    }

    @MessageMapping("/send-message/{chatId}")
    @SendTo("/topic/chat/{chatId}")
    public ChatMessage sendMessage(ChatMessage message) {
        //Kiểm tra coi đúng 2 userId ở cùng nhóm chát không

        // Logic lưu tin nhắn vào database
        ResponseServiceEntity<ChatMessage> result = chatMesageService.saveMessages(message);
        return result.getData();
    }

    @GetMapping("/PartialChatListFriend")
    public String PartialChatListFriend(Model model){
        Long userId = SecurityUtils.getLoggedInUserId();
        Users user = userService.getUserById(userId).getData();
        List<FriendWithUsernameDto> lstFriends = friendService.getFriendsWithUsernameByUserId1(userId).getData();
        List<Chats> chatGroups = chatService.getChatGroupsByUserId(userId).getData();


        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("friends", lstFriends);
        model.addAttribute("chatGroups", chatGroups);
        return "partial/chat_list_friend";
    }

    @GetMapping("/PartialChatMessageFriend")
    public String PartialChatMessageFriend(@RequestParam("userId") String userId,
                                           @RequestParam("chatId") String chatId,
                                           Model model){
        Long userId_1_Long = SecurityUtils.getLoggedInUserId();

        if(chatId == null || chatId.isEmpty()){
            return "partial/chat_message_friend_empty";
        }
        Chats chat = chatService.getChatById(Long.parseLong(chatId)).getData();
        if(chat == null){
            return "partial/chat_message_friend_empty";
        }
        Long chatId_Long = Long.parseLong(chatId);
        Boolean isGroup = chat.getIsGroupChat();
        //Nếu là group chat
        if(!chat.getIsGroupChat()){
            //Nếu là chat 1-1
            Long userId_2_Long = Long.parseLong(userId);
            //Get info userFriend
            Users friend = userService.getUserById(userId_2_Long).getData();
            model.addAttribute("chatName", friend.getFullName());
        }
        else{
            model.addAttribute("chatName", chat.getGroupName());
        }
        model.addAttribute("isGroup", isGroup);
        model.addAttribute("userIdLogin", userId_1_Long);
        model.addAttribute("chatId", chatId_Long);
        //Get messages
        List<Messages> messages = messagesService.GetAllMessagesByChatId(chatId_Long).getData();
        model.addAttribute("messageList", messages);


        return "partial/chat_message_friend";

    }

    @PostMapping("/createGroup")
    @ResponseBody
    public ResponseServiceEntity<Long> createGroupChat(@RequestParam("userIds") List<Long> userIds) {
        userIds.add(SecurityUtils.getLoggedInUserId());
        ResponseServiceEntity<Long> result = chatService.createGroupChat(userIds);
        return result;
    }

}
