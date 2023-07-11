package com.coderscampus.security.demo.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.coderscampus.security.demo.domain.RefreshToken;
import com.coderscampus.security.demo.domain.User;
import com.coderscampus.security.demo.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {
    
    private UserService userService;
    private RefreshTokenRepository refreshTokenRepository;
    
    @Value("${jwt.refreshTokenExpirationTimeInMillis}")
    private Long refreshTokenExpirationTimeInMillis;
    
    public RefreshTokenService(UserService userService, RefreshTokenRepository refreshTokenRepository) {
        super();
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken generateRefreshToken (Integer userId) {
        
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isPresent()) {
            RefreshToken refreshToken = new RefreshToken(userOpt.get(), UUID.randomUUID().toString(), new Date(System.currentTimeMillis() + refreshTokenExpirationTimeInMillis));
            refreshToken = refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }
        return null;
    }
}
