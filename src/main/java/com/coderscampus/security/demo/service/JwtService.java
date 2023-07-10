package com.coderscampus.security.demo.service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
    /** 
     * What we need to be able to do with JWTs in this class
     * 0. Create a JWT signing key (Done)
     * 1. Create / generate the JWT (Done)
     * 2. Extract claims (ie get stuff from the payload) ***
     * 3. Verify that the JWT valid ***
     * 4. Sign the JWT (Done)
     */
    @Value("${jwt.signingKey}")
    private String jwtSigningKey;
    @Value("${jwt.expirationTimeInMillis}")
    private Long expirationTimeInMillis;
    
    public Claims extractAllClaims (String token) {
        Claims body = Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
        return body;
    }
    
    public String getSubject (String token) {
        String subject = extractClaim(token, Claims::getSubject);
        return subject;
    }
    
    public Boolean isTokenValid (String token, UserDetails user) {
        String subject = getSubject(token);
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        return user.getUsername().equalsIgnoreCase(subject) && new Date().before(expirationDate);
    }
    
    private <R> R extractClaim (String token, Function<Claims, R> claimsExtract) {
        Claims allClaims = extractAllClaims(token);
        return claimsExtract.apply(allClaims);
    }
    
    public String generateToken(Map<String, Object> extraClaims, UserDetails user) {
        
       String jwt = Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(user.getUsername())
            .setIssuedAt(new Date())
            .setExpiration( new Date(System.currentTimeMillis() + expirationTimeInMillis) )
            .setHeaderParam("typ", Header.JWT_TYPE)
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
