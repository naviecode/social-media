package com.project.social_media.services;

import com.project.social_media.constants.ErrorCodes;
import com.project.social_media.models.ResponseServiceEntity;
import com.project.social_media.models.Users;
import com.project.social_media.repository.DatabaseConnection.DatabaseConnection;
import com.project.social_media.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;



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
