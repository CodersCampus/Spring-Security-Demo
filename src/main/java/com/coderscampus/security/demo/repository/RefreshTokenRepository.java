package com.coderscampus.security.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coderscampus.security.demo.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer>{

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
