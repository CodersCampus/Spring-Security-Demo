package com.coderscampus.security.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.coderscampus.security.demo.domain.User;

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
        System.out.println(jwt);
    }

}
