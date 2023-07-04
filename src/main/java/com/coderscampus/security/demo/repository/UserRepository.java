package com.coderscampus.security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coderscampus.security.demo.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    User findByUsername(String username);
}
