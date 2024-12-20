package com.project.social_media.controllers;


import com.project.social_media.dto.NotificationsDto;
import com.project.social_media.models.Users;
import com.project.social_media.patterns.Observer.FriendObserver;
import com.project.social_media.patterns.Observer.GroupObserver;
import com.project.social_media.patterns.Observer.NotificationSubject;
import com.project.social_media.services.UserService;
import com.project.social_media.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private UserService userService;

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

    // Gửi thông báo 1-1
    @MessageMapping("/send-to-user/{username}")
    public void sendToUser(NotificationsDto notify, @org.springframework.messaging.handler.annotation.DestinationVariable String username) {

        switch (notify.getType()) {
            case "MESSAGE":
                notify.setContent("Bạn có tin nhắn mới");
                break;
            case "ADD_MEMBER":
                notify.setContent("Bạn đã được thêm vào nhóm");
                break;
            default:
                notify.setContent("Thay đổi nhóm không xác định.");
        }

        FriendObserver friendObserver = new FriendObserver(username, messagingTemplate);
        notificationSubject.addObserver(friendObserver);

        notificationSubject.notifyObservers(notify);

        notificationSubject.removeObserver(friendObserver);
    }



}
