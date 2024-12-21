package com.project.social_media.controllers;


import com.project.social_media.constants.ErrorCodes;
import com.project.social_media.dto.*;
import com.project.social_media.models.*;
import com.project.social_media.services.*;
import com.project.social_media.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatMessageService chatMesageService;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private MessagesService messagesService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMemberService chatMemberService;


    @GetMapping
    public String chat(Model model)
    {
        Long userId = SecurityUtils.getLoggedInUserId();
        Users user = userService.getUserById(userId).getData();
        List<FriendWithUsernameDto> lstFriends = friendService.getFriendsWithUsernameByUserId1(userId, "").getData();
        List<FriendWithUsernameDto> noLstFriends = friendService.getFindNonFriendsWithUsernameByUserId1(userId, "").getData();
        List<ChatGroupWithUnreadCountDto> chatGroups = chatService.getChatGroupsByUserId(userId).getData();

        model.addAttribute("friends", lstFriends);
        model.addAttribute("noFriends", noLstFriends);
        model.addAttribute("chatGroups", chatGroups);
        model.addAttribute("userLogin", userId);
        model.addAttribute("fullName", user.getFullName());

        return "chat/chatview";
    }

    @MessageMapping("/send-message/{chatId}")
    @SendTo("/topic/chat/{chatId}")
    public ChatMessage sendMessage(ChatMessage message) {
        // Logic lưu tin nhắn vào database
        ResponseServiceEntity<ChatMessage> result = chatMesageService.saveMessages(message);
        return result.getData();
    }

    @GetMapping("/PartialChatListFriend")
    public String PartialChatListFriend(@RequestParam("name") String name, Model model){
        Long userId = SecurityUtils.getLoggedInUserId();
        Users user = userService.getUserById(userId).getData();
        List<FriendWithUsernameDto> lstFriends = friendService.getFriendsWithUsernameByUserId1(userId, name).getData();
        List<ChatGroupWithUnreadCountDto> chatGroups = chatService.getChatGroupsByUserId(userId).getData();
        List<FriendWithUsernameDto> noLstFriends = friendService.getFindNonFriendsWithUsernameByUserId1(userId, "").getData();

        model.addAttribute("userLogin", user);
        model.addAttribute("friends", lstFriends);
        model.addAttribute("noFriends", noLstFriends);
        model.addAttribute("chatGroups", chatGroups);
        model.addAttribute("oldChat", name);
        return "partial/chat_list_friend";
    }

    @GetMapping("/PartialChatMessageFriend")
    public String PartialChatMessageFriend(@RequestParam("userId") String userId,
                                           @RequestParam("chatId") String chatId,
                                           Model model){
        Long userId_1_Long = SecurityUtils.getLoggedInUserId();
        List<Users> lstMembmer = new ArrayList<Users>();

        if(chatId == null || chatId.isEmpty()){
            return "partial/chat_message_friend_empty";
        }
        Chats chat = chatService.getChatById(Long.parseLong(chatId)).getData();
        if(chat == null){
            return "partial/chat_message_friend_empty";
        }
        Long chatId_Long = Long.parseLong(chatId);
        Boolean isGroup = chat.getIsGroupChat();
        //Nếu là chat 1 - 1
        if(!chat.getIsGroupChat()){
            Long userId_2_Long = Long.parseLong(userId);
            Users friend = userService.getUserById(userId_2_Long).getData();
            model.addAttribute("chatName", friend.getFullName());
            model.addAttribute("chatAvatar", friend.getAvatarURL());
            messagesService.markMessagesAsReadByChatIdAndSenderId(chatId_Long, userId_1_Long);
            lstMembmer.add(friend);
        }
        else{
            lstMembmer.addAll(chatMemberService.getUsersByChatId(chatId_Long,userId_1_Long).getData());
            model.addAttribute("chatName", chat.getGroupName());
        }

        model.addAttribute("isGroup", isGroup);
        model.addAttribute("userIdLogin", userId_1_Long);
        model.addAttribute("userIdReceive", userId);
        model.addAttribute("chatId", chatId_Long);
        model.addAttribute("chat", chat);
        List<MessageWithSenderNameDto> messages = messagesService.GetAllMessagesByChatId(chatId_Long).getData();
        model.addAttribute("messageList", messages);
        model.addAttribute("members", lstMembmer);


        return "partial/chat_message_friend";

    }

    @PostMapping("/createGroup")
    @ResponseBody
    public ResponseServiceEntity<Long> createGroupChat(@RequestParam("userIds") List<Long> userIds) {
        userIds.add(SecurityUtils.getLoggedInUserId());
        String groupName = userService.getUserNamesByIds(userIds);
        ResponseServiceEntity<Long> result = chatService.createGroupChat(userIds, groupName);
        return result;
    }

    @PostMapping("createChat")
    @ResponseBody
    public ResponseServiceEntity<String> createChat(@RequestParam("user_id") Long userId) {
        Long userLogin = SecurityUtils.getLoggedInUserId();
        Long chatIdTwoUser = chatMemberService.GetChatIdTwoUser(userLogin, userId).getData();
        if(chatIdTwoUser != null){
            return ResponseServiceEntity.success("Đã được tạo nhom chat", ErrorCodes.SUCCESS);
        }
        Long chatId = chatService.insertChat(null, false).getData();
        chatMemberService.insertChatMember(chatId, userLogin);
        chatMemberService.insertChatMember(chatId, userId);
        return ResponseServiceEntity.success("Tạo thành công", ErrorCodes.SUCCESS);
    }

    @PostMapping("/changeGroupName")
    @ResponseBody
    public ResponseServiceEntity<String> changeGroupName(@RequestParam("chatId") Long chatId, @RequestParam("newGroupName") String newGroupName) {
        return chatService.changeGroupName(chatId, newGroupName);
    }

    @PostMapping("/removeMember")
    @ResponseBody
    public ResponseServiceEntity<String> removeMember(@RequestParam("chatId") Long chatId, @RequestParam("userId") Long userId) {
        return chatService.removeMember(chatId, userId);
    }

    @PostMapping("/leaveGroup")
    @ResponseBody
    public ResponseServiceEntity<String> leaveGroup(@RequestParam("chatId") Long chatId, @RequestParam("userId") Long userId) {
        return chatService.leaveGroup(chatId, userId);
    }

    @PostMapping("/deleteChat")
    @ResponseBody
    public ResponseServiceEntity<String> deleteChat(@RequestParam("chatId") Long chatId, @RequestParam("userId") Long userId) {
        return chatService.deleteChat(chatId, userId);
    }

    @PostMapping("/addMember")
    @ResponseBody
    public ResponseServiceEntity<String> addMember(@RequestParam("chatId") Long chatId, @RequestParam("userName") String userName) {
        return chatService.addMember(chatId, userName);
    }

}
