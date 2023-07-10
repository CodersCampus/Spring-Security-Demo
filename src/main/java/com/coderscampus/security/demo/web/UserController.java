package com.coderscampus.security.demo.web;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coderscampus.security.demo.domain.User;
import com.coderscampus.security.demo.repository.UserRepository;
import com.coderscampus.security.demo.response.AuthenticationResponse;
import com.coderscampus.security.demo.service.JwtService;
import com.coderscampus.security.demo.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private UserService userService;
    
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            UserService userService) {
        super();
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<AuthenticationResponse> signUpUser (@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        
        String accessToken = jwtService.generateToken(new HashMap<>(), savedUser);
        
        return ResponseEntity.ok(new AuthenticationResponse(savedUser.getUsername(), accessToken));
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> signInUser(@RequestBody User user) {
        UserDetails loggedInUser = userService.loadUserByUsername(user.getUsername());
        String accessToken = jwtService.generateToken(new HashMap<>(), loggedInUser);
        
        return ResponseEntity.ok(new AuthenticationResponse(loggedInUser.getUsername(), accessToken));
    }
}
