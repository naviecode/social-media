package com.project.social_media.controllers;


import com.project.social_media.dto.NotificationsDto;
import com.project.social_media.models.Chats;
import com.project.social_media.models.Notifications;
import com.project.social_media.models.Users;
import com.project.social_media.patterns.Observer.FriendObserver;
import com.project.social_media.patterns.Observer.GroupObserver;
import com.project.social_media.patterns.Observer.NotificationSubject;
import com.project.social_media.services.*;
import com.project.social_media.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationsService notificationsService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private ChatMemberService chatMemberService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private NotificationSubject notificationSubject = new NotificationSubject();

    // Gửi thông báo đến nhóm
    @MessageMapping("/send-to-group/{chatId}")
    public void sendToGroup(NotificationsDto notify, @org.springframework.messaging.handler.annotation.DestinationVariable Long chatId) {
        Long userId = SecurityUtils.getLoggedInUserId();

        switch (notify.getType()) {
            case "MESSAGE":
                notify.setContent("Có tin nhắn mới từ nhóm chat");
                break;
            case "RENAME_GROUP":
                notify.setContent("Nhóm đã được đổi tên thành: " + notify.getContent());
                break;
            case "ADD_MEMBER":
                Users user = userService.getUserByUserName(notify.getAttr1()).getData();
                notify.setContent(notify.getFullName() + " đã thêm " + user.getFullName() + " vào nhóm");
                break;
            case "REMOVE_MEMBER":
                //Nếu user bị xóa bằng user login thì sẽ đổi text là bạn đã bị xóa khỏi nhóm
                Long userRemove = Long.parseLong(notify.getAttr1());
                Users userRemoveObj = userService.getUserById(userRemove).getData();
                if (userRemove.equals(userId)) {
                    notify.setContent("Bạn đã bị xóa khỏi nhóm");
                    break;
                }
                notify.setContent(notify.getFullName() + " đã xóa " + userRemoveObj.getFullName() + " ra khỏi nhóm");
                break;
            case "LEAVE_GROUP":
                notify.setContent(notify.getFullName() + " đã rời khỏi nhóm");
                break;
            case "DELETE_GROUP":
                //Chỉ có người tạo mới được xóa nhóm
                notify.setContent(notify.getFullName() + "đã xóa nhóm");
                break;
            default:
                notify.setContent("Thay đổi nhóm không xác định.");
        }


        GroupObserver groupObserver = new GroupObserver(chatId, messagingTemplate);
        notificationSubject.addObserver(groupObserver);

        notificationSubject.notifyObservers(notify);

        notificationSubject.removeObserver(groupObserver);
    }

    @MessageMapping("/send-to-user/{username}")
    public void sendToUser(NotificationsDto notify, @org.springframework.messaging.handler.annotation.DestinationVariable String username) {

        switch (notify.getType()) {
            case "MESSAGE":
                notify.setContent("Bạn có tin nhắn mới");
                break;
            case "ADD_MEMBER":
                notify.setContent("Bạn đã được thêm vào nhóm");
                break;
            case "ADD_FRIEND":
                Users user = userService.getUserById(notify.getSenderId()).getData();
                if(user == null) {
                    notify.setContent("Người gửi không tồn tại");
                    break;
                }
                if(!friendService.hasPendingFriendRequest(Long.parseLong(notify.getAttr1()),notify.getSenderId()).getData())
                {
                    Notifications notifications = new Notifications(user.getUserId(),null  , userService.getUserById(Long.parseLong(notify.getAttr1())).getData().getFullName() + " đã gửi lời mời kết bạn", "personal");
                    friendService.addFriend(Long.parseLong(notify.getAttr1()) ,notify.getSenderId());
                    notificationsService.insertNotification(notifications);
                    notify.setContent("Bạn có lời mời kết bạn đang chờ duyệt");
                }

                if(friendService.hasPendingFriendRequest(Long.parseLong(notify.getAttr1()),notify.getSenderId()).getData() &&
                    friendService.hasPendingFriendRequest(notify.getSenderId(),Long.parseLong(notify.getAttr1())).getData()) {
                    notify.setContent("Bạn và " + user.getFullName() + " đã trở thành bạn bè");
                    friendService.updateFriendStatus(Long.parseLong(notify.getAttr1()), notify.getSenderId(), "accepted");
                    friendService.updateFriendStatus(notify.getSenderId(), Long.parseLong(notify.getAttr1()), "accepted");
                    notificationsService.updateNotificationStatusByUserId(Long.parseLong(notify.getAttr1()), true);
                    notificationsService.updateNotificationStatusByUserId(notify.getSenderId(), true);
                    Long chatId = chatService.insertChat(null, false).getData();
                    chatMemberService.insertChatMember(chatId, Long.parseLong(notify.getAttr1()));
                    chatMemberService.insertChatMember(chatId, notify.getSenderId());
                    break;
                }
                notify.setContent(userService.getUserById(Long.parseLong(notify.getAttr1())).getData().getFullName() + " đã gửi lời mời kết bạn");
                break;
            case "ACCEPT_FRIEND":
                if(friendService.areFriends(Long.parseLong(notify.getAttr1()), notify.getSenderId()).getData()){
                    notify.setContent("Đã là bạn bè");
                    break;
                }
                friendService.addFriend(Long.parseLong(notify.getAttr1()) ,notify.getSenderId());
                friendService.updateFriendStatus(Long.parseLong(notify.getAttr1()), notify.getSenderId(), "accepted");
                friendService.updateFriendStatus(notify.getSenderId(), Long.parseLong(notify.getAttr1()), "accepted");
                Long chatId = chatService.insertChat(null, false).getData();
                chatMemberService.insertChatMember(chatId, Long.parseLong(notify.getAttr1()));
                chatMemberService.insertChatMember(chatId, notify.getSenderId());
                notify.setContent(userService.getUserById(Long.parseLong(notify.getAttr1())).getData().getFullName() + " đã chấp nhận lời mời kết bạn");
                break;
            case "CANCEL_FRIEND":
                notify.setContent(userService.getUserById(Long.parseLong(notify.getAttr1())).getData().getFullName() + " đã từ chối kết bạn vì ghét bạn");
                friendService.updateFriendStatus(notify.getSenderId(), Long.parseLong(notify.getAttr1()), "deny");
                break;
            case "UNFRIEND":
                if(!friendService.areFriends(Long.parseLong(notify.getAttr1()), notify.getSenderId()).getData()){
                    notify.setContent("Không phải bạn bè");
                    break;
                }
                Long chatIdTwoUser = chatMemberService.GetChatIdTwoUser(Long.parseLong(notify.getAttr1()), notify.getSenderId()).getData();
                chatMemberService.deleteChatMembersByChatId(chatIdTwoUser);
                chatService.deleteChatById(chatIdTwoUser);
                friendService.deleteFriend(Long.parseLong(notify.getAttr1()), notify.getSenderId());
                friendService.deleteFriend(notify.getSenderId(), Long.parseLong(notify.getAttr1()));
                notify.setContent(userService.getUserById(Long.parseLong(notify.getAttr1())).getData().getFullName() + " đã hủy kết bạn vì không là gì của nhau");
                break;
            default:
                notify.setContent("Thay đổi không xác định.");
        }

        FriendObserver friendObserver = new FriendObserver(username, messagingTemplate);
        notificationSubject.addObserver(friendObserver);
        notificationSubject.notifyObservers(notify);
        notificationSubject.removeObserver(friendObserver);
    }
    @MessageMapping("/send-to-friends/{username}")
    public void sendToFriends(NotificationsDto notify, @org.springframework.messaging.handler.annotation.DestinationVariable String username) {
        Long userIdLogin = SecurityUtils.getLoggedInUserId();


        FriendObserver friendObserver = new FriendObserver(username, messagingTemplate);
        notificationSubject.addObserver(friendObserver);
        notificationSubject.notifyObservers(notify);
        notificationSubject.removeObserver(friendObserver);

    }

    @PutMapping("/update-status/{notificationId}")
    public void updateNotificationStatus(@PathVariable Long notificationId, @RequestParam Boolean isRead) {
        notificationsService.updateNotificationStatus(notificationId, isRead);
    }

    @PutMapping("/update-status-by-user/{userId}")
    public void updateNotificationStatusByUserId(@PathVariable Long userId, @RequestParam Boolean isRead) {
        notificationsService.updateNotificationStatusByUserId(userId, isRead);
    }

    @GetMapping("/user")
    public List<Notifications> getUserNotifications() {
        Long userId = SecurityUtils.getLoggedInUserId();
        return notificationsService.getNotificationsByUserId(userId);
    }


}
