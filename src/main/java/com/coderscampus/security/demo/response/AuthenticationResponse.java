package com.coderscampus.security.demo.response;

public record AuthenticationResponse(
        String username,
        String token) {

}
