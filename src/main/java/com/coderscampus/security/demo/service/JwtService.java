package com.coderscampus.security.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtService {
    
    /** 
     * What we need to be able to do with JWTs in this class
     * 0. Create a JWT signing key (Done)
     * 1. Create / generate the JWT
     * 2. Extract claims (ie get stuff from the payload)
     * 3. Verify that the JWT valid
     * 4. Sign the JWT
     */
    @Value("${jwt.signingKey}")
    private String jwtSigningKey;
    
    public String generateToken(UserDetails user) {
        Jwts.builder()
            .setClaims(null)
            .signWith(null, SignatureAlgorithm.HS256);
    }
    
}
