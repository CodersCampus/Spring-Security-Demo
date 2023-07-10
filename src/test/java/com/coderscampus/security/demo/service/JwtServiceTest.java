package com.coderscampus.security.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.coderscampus.security.demo.domain.User;

import io.jsonwebtoken.Claims;

@TestInstance(Lifecycle.PER_CLASS)
//@SpringBootTest
class JwtServiceTest {

//    @Autowired
    private JwtService sut;
    
    @BeforeAll
    void init () {
        sut = new JwtService();
        sut.setExpirationTimeInMillis(300000L);
        sut.setJwtSigningKey("31313B6BFF8BE89217A99A345C9911CAA2A80915ED93193F52E158CDBCEAEAFC");
    }
    
    @Test
    @DisplayName("should generate a new JWS token")
    void testGenerateToken() {
        /** 
         * 1. Arrange
         * 2. Act
         * 3. Assert
         */
                
        // Arrange
        Map<String, Object> extraClaims = new HashMap<>();
        User user = new User("trevor@coderscampus.com", "abc123");
        
        // Act
        String jwt = sut.generateToken(extraClaims, user);
        
        // Assert
        assertTrue(jwt.startsWith("ey"));
            
    }
    
    @Test
    @DisplayName("should extract all claims")
    void testExtractAllClaims () {
        // Arrange
        Map<String, Object> extraClaims = new HashMap<>();
        User user = new User("trevor@coderscampus.com", "abc123");
        String token = sut.generateToken(extraClaims, user);
        
        // Act
        Claims allClaims = sut.extractAllClaims(token);
        
        assertEquals("trevor@coderscampus.com", allClaims.getSubject());
        
    }

}
