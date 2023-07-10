package com.coderscampus.security.demo.security;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.coderscampus.security.demo.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Requests: 
        //  Headers -> key/value pairs (Authorization -> Bearer xxx.yyy.zzz)
        //  Body -> (if JSON) key/value pairs
        String auth = request.getHeader("Authorization");
        
        if (!auth.isEmpty()) {
            // hey, we have a token (probably) in the request
            // let's see if this token is a valid JWS or not
            
            
            // jwtService.getSubject(auth);
            //jwtService.isValidToken(auth, null);
        }
    }

}
