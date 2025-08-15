package com.ojo.mullyuojo.domain.user.dto;

import lombok.Getter;

@Getter
public class AuthResponseDto {
    String message;

    public AuthResponseDto(String message) {
        this.message = message;
    }
}
