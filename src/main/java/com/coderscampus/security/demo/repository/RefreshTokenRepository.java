package com.coderscampus.security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coderscampus.security.demo.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer>{

}
