package com.coderscampus.security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.coderscampus.security.demo.domain.User;

public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = new User(username, "abc123");
        
        return user;
    }

}
