package com.ojo.mullyuojo.slackmessage;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SlackResponseDto {
    String sender;
    String recipient;
    String message;
    LocalDateTime createdAt;


    public SlackResponseDto(SlackMessage message) {
        this.sender = message.getSender();
        this.recipient = message.getRecipient();
        this.message = message.getMessage();
        this.createdAt = message.getCreatedAt();
    }
}
