package com.coderscampus.security.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import com.coderscampus.security.demo.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

// Example URL -> http://localhost:8080/products
    
    @Bean
    public UserDetailsService userDetailsService () {
        return new UserService();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((request) -> {
            request.requestMatchers("/products").authenticated()
                   .anyRequest().permitAll();
        })
        .userDetailsService(userDetailsService())
        .formLogin(Customizer.withDefaults());
        
//        authorizeHttpRequests().requestMatchers("/public/**").permitAll().anyRequest()
//                .hasRole("USER").and()
//                // Possibly more configuration ...
//                .formLogin() // enable form based log in
//                // set permitAll for all URLs associated with Form Login
//                .permitAll();
        return http.build();
    }

}
