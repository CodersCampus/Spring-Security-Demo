package com.coderscampus.security.demo.security;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.coderscampus.security.demo.domain.RefreshToken;
import com.coderscampus.security.demo.domain.User;
import com.coderscampus.security.demo.repository.UserRepository;
import com.coderscampus.security.demo.service.JwtService;
import com.coderscampus.security.demo.service.RefreshTokenService;
import com.coderscampus.security.demo.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

// Example URL -> http://localhost:8080/products
    private UserRepository userRepository;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private JwtService jwtService;
    private RefreshTokenService refreshTokenService;
    
    public SecurityConfiguration(UserRepository userRepository, JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtService jwtService, RefreshTokenService refreshTokenService) {
        super();
        this.userRepository = userRepository;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public UserDetailsService userDetailsService () {
        return new UserService(userRepository);
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests((request) -> {
            request
                   .requestMatchers("/api/v1/users", "/api/v1/users/**").permitAll()
                   .anyRequest().authenticated();
        })
        .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
          .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .formLogin(login -> {
            login.loginPage("/login");
            login.failureUrl("/login-error");
            login.successHandler(new AuthenticationSuccessHandler() {
                
                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {
                    User user = (User) authentication.getPrincipal();
                    
                    String accessToken = jwtService.generateToken(new HashMap<>(), user);
                    RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user.getId());
                    
                    Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
                    Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken.getRefreshToken());
                    
                    response.addCookie(accessTokenCookie);
                    response.addCookie(refreshTokenCookie);
                    response.sendRedirect("/products");
                }
            });
            login.permitAll();
        });
        
        return http.build();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider () {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }

}
