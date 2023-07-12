package com.coderscampus.security.demo.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.coderscampus.security.demo.request.RefreshTokenRequest;
import com.coderscampus.security.demo.service.JwtService;
import com.coderscampus.security.demo.service.RefreshTokenService;
import com.coderscampus.security.demo.service.UserService;
import com.coderscampus.security.demo.util.CookieUtils;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UserService userService;
    private RefreshTokenService refreshTokenService;

    public JwtAuthenticationFilter(JwtService jwtService, UserService userService,
            RefreshTokenService refreshTokenService) {
        super();
        this.jwtService = jwtService;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Requests: 
        //  Headers -> key/value pairs (Authorization -> Bearer xxx.yyy.zzz)
        //  Body -> (if JSON) key/value pairs
        Cookie accessTokenCookie = null;
        Cookie refreshTokenCookie = null;
        
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(CookieUtils.ACCESS_TOKEN_NAME)) {
                    accessTokenCookie = cookie;
                } else if (cookie.getName().equals(CookieUtils.REFRESH_TOKEN_NAME)) {
                    refreshTokenCookie = cookie;
                }
            }
        }
        
        if (accessTokenCookie != null) {
            // hey, we have a token (probably) in the request
            // let's see if this token is a valid JWS or not
            
            int loginTryCount = 0;
            
            while (loginTryCount <= 2) {
                String token = accessTokenCookie.getValue();
                try {
                    String subject = jwtService.getSubject(token);
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    
                    if (StringUtils.hasText(subject) && authentication == null) {
                        UserDetails userDetails = userService.loadUserByUsername(subject);
                        
                        if (jwtService.isValidToken(token, userDetails)) {
                            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, 
                                    userDetails.getPassword(),
                                    userDetails.getAuthorities());
                            securityContext.setAuthentication(authToken);
                            SecurityContextHolder.setContext(securityContext);
                            break;
                        } 
                    }
                } catch (ExpiredJwtException e) {
                    try {
                        token = refreshTokenService.createNewAccessToken(new RefreshTokenRequest(refreshTokenCookie.getValue()));
                        accessTokenCookie = CookieUtils.createAccessTokenCookie(token);
                        
                        response.addCookie(accessTokenCookie);
                    } catch (Exception e1) {
                        // there was a problem creating a new access token, 
                        //  we're ignore this error on purpose in order to allow
                        //  the flow of the filterChain to continue
                    }
                }
                loginTryCount++;
            }
        }
        filterChain.doFilter(request, response);
    }

}
