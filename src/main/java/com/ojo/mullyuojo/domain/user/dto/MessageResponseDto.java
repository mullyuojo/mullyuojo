package com.ojo.mullyuojo.domain.user.dto;

import lombok.Getter;

@Getter
public class MessageResponseDto {
    String message;

    public MessageResponseDto(String message) {
        this.message = message;
    }
}
