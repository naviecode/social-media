package com.project.social_media.services;

import com.project.social_media.constants.ErrorCodes;
import com.project.social_media.dto.UserInfoDto;
import com.project.social_media.dto.ResponseServiceEntity;
import com.project.social_media.models.Users;
import com.project.social_media.repository.FriendsRepository;
import com.project.social_media.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private FriendsRepository friendsRepository;


    public ResponseServiceEntity<Users> getUserById(Long id) {
        Users user = usersRepository.findById(id).orElse(null);
        if(user == null) {
            return ResponseServiceEntity.error(ErrorCodes.ERROR_USER_NOT_EXISTS);
        }
        return ResponseServiceEntity.success(user,ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<Users> getUserByUserName(String userName){
        Users user = usersRepository.findByUsername(userName).orElse(null);
        if(user == null) {
            return ResponseServiceEntity.error(ErrorCodes.ERROR_USER_NOT_EXISTS);
        }
        return ResponseServiceEntity.success(user,ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<UserInfoDto> getUserInfo(Long userId) {
        Users user = usersRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseServiceEntity.error(ErrorCodes.ERROR_USER_NOT_EXISTS);
        }
        long numberOfFriends = friendsRepository.countByUserId(userId);
        UserInfoDto userInfoDto = new UserInfoDto(user.getFullName(), user.getUsername(), numberOfFriends, user.getAvatarURL());

        return ResponseServiceEntity.success(userInfoDto, ErrorCodes.SUCCESS);
    }

    public List<Users> searchUsersByName(String name, Long userId) {
        return usersRepository.findByNameContaining(name, userId );
    }


    public String getUserNamesByIds(List<Long> userIds) {
        List<Users> users = usersRepository.findAllById(userIds);
        return users.stream()
                .map(Users::getFullName)
                .collect(Collectors.joining(", "));
    }

    public String getUserStatus(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getIsActive()) {
            return "Online";
        }

        LocalDateTime lastActive = user.getLastActive();
        if (lastActive == null) {
            return "Offline for an unknown duration";
        }

        Duration duration = Duration.between(lastActive, LocalDateTime.now());
        long minutesOffline = duration.toMinutes();

        return "Offline for " + minutesOffline + " minute(s)";
    }

    public void updateActivity(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setLastActive(LocalDateTime.now());
        user.setIsActive(true);
        usersRepository.save(user);
    }

    public void markUserInactive(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(false);
        usersRepository.save(user);
    }

    public ResponseServiceEntity<String> saveAvatarUrl(Long userId, String avatarUrl) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setAvatarURL(avatarUrl);
        usersRepository.save(user);
        return ResponseServiceEntity.success(avatarUrl, ErrorCodes.SUCCESS);
    }

//    @Autowired
//    private DatabaseConnection databaseConnection;

    // Sử dụng JPA Repository
//    public Optional<Users> getUserByUsername(String username) {
//        return usersRepository.findByUsername(username);
//    }

    // Sử dụng JDBC trực tiếp qua DatabaseConnection
//    public Users getUserDetailsById(Long userId) {
//        String query = "SELECT * FROM users WHERE id = ?";
//        try (Connection connection = databaseConnection.getConnection();
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//
//            stmt.setLong(1, userId);
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                Users user = new Users();
//                user.setUserId(rs.getLong("id"));
//                user.setUsername(rs.getString("username"));
//                return user;
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Error querying database", e);
//        }
//        return null;
//    }
}
