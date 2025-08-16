package com.ojo.mullyuojo.domain.user.dto;

import lombok.Getter;

@Getter
public class AuthResponseDto {
    String message;
    String accessToken;
    String refreshToken;

    public AuthResponseDto(String message) {
        this.message = message;
    }
    public AuthResponseDto(String message, String accessToken, String refreshToken) {
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
