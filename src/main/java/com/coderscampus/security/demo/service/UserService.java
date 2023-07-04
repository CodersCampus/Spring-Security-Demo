package com.coderscampus.security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.coderscampus.security.demo.domain.User;

public class UserService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;
    
    public UserService(PasswordEncoder passwordEncoder) {
        super();
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = new User(username, passwordEncoder.encode("abc123"));
        
        return user;
    }

}
