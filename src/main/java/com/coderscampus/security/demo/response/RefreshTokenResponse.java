package com.coderscampus.security.demo.response;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken) {

}
