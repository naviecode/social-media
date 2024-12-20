package com.project.social_media.services;

import com.project.social_media.Authorize.JwtUtils;
import com.project.social_media.constants.ErrorCodes;
import com.project.social_media.models.ResponseServiceEntity;
import com.project.social_media.models.Users;
import com.project.social_media.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    public ResponseServiceEntity<Users> register(Users user) {

        if(usersRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseServiceEntity.error(ErrorCodes.ERROR_AUTH_EXISTS_USERNAME);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
        return ResponseServiceEntity.success(user,ErrorCodes.SUCCESS);
    }

    public ResponseServiceEntity<String> login(String username, String password) {
        Users user = usersRepository.findByUsername(username).orElse(null);
        if(user == null){
            return ResponseServiceEntity.error(ErrorCodes.ERROR_AUTH_USERNAME_NOTFOUND);
        }
        if(!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseServiceEntity.error(ErrorCodes.ERROR_AUTH_LOGIN_FAIL);
        }
        return ResponseServiceEntity.success(jwtUtils.generateJwtToken(user.getUsername(), user.getUserId()),ErrorCodes.SUCCESS);
    }

}
