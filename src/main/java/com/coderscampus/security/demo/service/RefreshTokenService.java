package com.coderscampus.security.demo.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.coderscampus.security.demo.domain.RefreshToken;
import com.coderscampus.security.demo.domain.User;

@Service
public class RefreshTokenService {
    
    private UserService userService;
    
    @Value("${jwt.refreshTokenExpirationTimeInMillis}")
    private Long refreshTokenExpirationTimeInMillis;
    
    public RefreshTokenService(UserService userService) {
        super();
        this.userService = userService;
    }

    public RefreshToken generateRefreshToken (Integer userId) {
        
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isPresent()) {
            return new RefreshToken(null, userOpt.get(), UUID.randomUUID().toString(), new Date(System.currentTimeMillis() + refreshTokenExpirationTimeInMillis));
        }
        return null;
    }
}
