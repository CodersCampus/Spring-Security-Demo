package com.coderscampus.security.demo.service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
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
    @Value("${jwt.expirationTimeInMillis}")
    private Long expirationTimeInMillis;
    
    public String generateToken(Map<String, Object> extraClaims, UserDetails user) {
        
       String jwt = Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(user.getUsername())
            .setIssuedAt(new Date())
            .setExpiration( new Date(System.currentTimeMillis() + expirationTimeInMillis) )
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
        return jwt;
    }

    private Key getSigningKey() {
        byte[] jwtSigningKeyAsBytes = Decoders.BASE64.decode(jwtSigningKey);
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtSigningKeyAsBytes);
        return secretKey;
    }

    public void setJwtSigningKey(String jwtSigningKey) {
        if (this.jwtSigningKey == null)
            this.jwtSigningKey = jwtSigningKey;
        
    }

    public void setExpirationTimeInMillis(Long expirationTimeInMillis) {
        if (this.expirationTimeInMillis == null)
            this.expirationTimeInMillis = expirationTimeInMillis;
    }
    
}
