package com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SlackRequestDto {
    String sender;
    String recipient;
    String message;
    LocalDateTime createdAt;

}
