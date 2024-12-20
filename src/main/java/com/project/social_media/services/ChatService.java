package com.project.social_media.services;

import com.project.social_media.constants.ErrorCodes;
import com.project.social_media.dto.ChatGroupWithUnreadCountDto;
import com.project.social_media.dto.ResponseServiceEntity;
import com.project.social_media.dto.ResponseServiceListEntity;
import com.project.social_media.models.*;
import com.project.social_media.repository.ChatMemberRepository;
import com.project.social_media.repository.ChatRepository;
import com.project.social_media.repository.FriendsRepository;
import com.project.social_media.repository.UsersRepository;
import com.project.social_media.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMemberRepository chatMembersRepository;

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private UsersRepository usersRepository;


    public ResponseServiceEntity<Long> createGroupChat(List<Long> userIds, String groupName) {

        if (userIds.size() < 3) {
            return ResponseServiceEntity.error(ErrorCodes.ERROR_CHAT_ADD_GROUP_SIZE);
        }

        Chats newChat = new Chats();
        newChat.setGroupName(groupName);
        newChat.setIsGroupChat(true);
        newChat.setCreatedAt(LocalDateTime.now());
        newChat.setUserIdCreated(SecurityUtils.getLoggedInUserId());
        Chats savedChat = chatRepository.save(newChat);

        for (Long userId : userIds) {
            ChatMembers chatMember = new ChatMembers(savedChat.getChatId(), userId, LocalDateTime.now());
            chatMembersRepository.save(chatMember);
        }

        return ResponseServiceEntity.success(savedChat.getChatId(), ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<Long> insertChat(String groupName, Boolean isGroupChat) {
        Chats newChat = new Chats();
        newChat.setGroupName(groupName);
        newChat.setIsGroupChat(isGroupChat);
        newChat.setCreatedAt(LocalDateTime.now());
        Chats savedChat = chatRepository.save(newChat);
        return ResponseServiceEntity.success(savedChat.getChatId(), ErrorCodes.SUCCESS);
    }

    public ResponseServiceListEntity<ChatGroupWithUnreadCountDto> getChatGroupsByUserId(Long userId) {
        List<ChatGroupWithUnreadCountDto> chatGroups = chatRepository.findChatGroupsByUserIdWithUnreadCount(userId);
        return ResponseServiceListEntity.success(chatGroups,chatGroups.stream().count() ,ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<Chats> getChatById(Long chatId) {
        Chats chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            return ResponseServiceEntity.error(ErrorCodes.ERROR_CHAT_NOT_FOUND);
        }
        return ResponseServiceEntity.success(chat, ErrorCodes.SUCCESS);
    }
    public ResponseServiceEntity<String> changeGroupName(Long chatId, String newGroupName) {
        Long userId_1_Long = SecurityUtils.getLoggedInUserId();

        Chats chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            return ResponseServiceEntity.error(ErrorCodes.ERROR_CHAT_NOT_FOUND);
        }

        if(!chatMembersRepository.isMemberOfChat(chatId, userId_1_Long)){
            return ResponseServiceEntity.error(ErrorCodes.ERROR_CHAT_NOT_MEMBER);
        }

        chat.setGroupName(newGroupName);
        chatRepository.save(chat);
        return ResponseServiceEntity.success("Group name updated successfully", ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<String> removeMember(Long chatId, Long userId) {
        Long userId_1_Long = SecurityUtils.getLoggedInUserId();
        ChatMembers chatMember = chatMembersRepository.findByChatIdAndUserId(chatId, userId);

        if(!chatMembersRepository.isMemberOfChat(chatId, userId_1_Long)){
            return ResponseServiceEntity.error(ErrorCodes.ERROR_CHAT_NOT_MEMBER);
        }

        if(chatRepository.findById(chatId).orElse(null).getUserIdCreated().equals(userId)){
            return ResponseServiceEntity.error(ErrorCodes.ERROR_CHAT_MEMBER_NOT_REMOVE_OWNER);
        }

        if (chatMember == null) {
            return ResponseServiceEntity.error(ErrorCodes.ERROR_USER_NOT_EXISTS);
        }
        chatMembersRepository.delete(chatMember);

        //Nếu khi xóa xong chỉ còn 2 user thì xóa nhóm chat luôn tránh xung đột
        if(chatMembersRepository.countMembersByChatId(chatId) == 2){
            Chats chat = chatRepository.findById(chatId).orElse(null);
            if(chat == null){
                return ResponseServiceEntity.error(ErrorCodes.ERROR_CHAT_NOT_FOUND);
            }
            chatRepository.delete(chat);
            return ResponseServiceEntity.success("Delete chat", ErrorCodes.SUCCESS);
        }

        return ResponseServiceEntity.success("Member removed successfully", ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<String> leaveGroup(Long chatId, Long userId) {

        ChatMembers chatMember = chatMembersRepository.findByChatIdAndUserId(chatId, userId);
        if(!chatMembersRepository.isMemberOfChat(chatId, SecurityUtils.getLoggedInUserId())){
            return ResponseServiceEntity.error(ErrorCodes.ERROR_CHAT_NOT_MEMBER);
        }

        if (chatMember == null) {
            return ResponseServiceEntity.error(ErrorCodes.ERROR_USER_NOT_EXISTS);
        }
        chatMembersRepository.delete(chatMember);
        return ResponseServiceEntity.success("Left group successfully", ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<String> deleteChat(Long chatId, Long userId) {
        Chats chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            return ResponseServiceEntity.error(ErrorCodes.ERROR_CHAT_NOT_FOUND);
        }
        if (!chat.getUserIdCreated().equals(userId)) {
            return ResponseServiceEntity.error(ErrorCodes.ERROR_CHAT_NOT_CREATED);
        }
        chatRepository.delete(chat);
        return ResponseServiceEntity.success("Chat deleted successfully", ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<String> addMember(Long chatId, String userName) {
        Long userIdLogin = SecurityUtils.getLoggedInUserId();
        Users userAdd = usersRepository.findByUsername(userName).orElse(null);

        if(userAdd == null){
            return ResponseServiceEntity.error(ErrorCodes.ERROR_USER_NOT_EXISTS);
        }

        if(!chatMembersRepository.isMemberOfChat(chatId, userIdLogin)){
            return ResponseServiceEntity.error(ErrorCodes.ERROR_CHAT_NOT_MEMBER);
        }
        if(!friendsRepository.isFriend(userIdLogin, userAdd.getUserId())){
            return ResponseServiceEntity.error(ErrorCodes.ERROR_CHAT_MEMBER_NOT_EXISTS_FRIEND);
        }

        if(chatMembersRepository.isMemberOfChat(chatId, userAdd.getUserId())){
            return ResponseServiceEntity.error(ErrorCodes.ERROR_CHAT_MEMBER_EXISTS);
        }

        ChatMembers chatMember = new ChatMembers(chatId, userAdd.getUserId(), LocalDateTime.now());
        chatMembersRepository.save(chatMember);
        return ResponseServiceEntity.success("Member added successfully", ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<Void> deleteChatById(Long chatId) {
        chatRepository.deleteById(chatId);
        return ResponseServiceEntity.success(null, ErrorCodes.SUCCESS);
    }

}
