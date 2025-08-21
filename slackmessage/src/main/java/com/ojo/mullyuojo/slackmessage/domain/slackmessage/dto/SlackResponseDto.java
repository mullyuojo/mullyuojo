package com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto;

import com.ojo.mullyuojo.slackmessage.domain.slackmessage.SlackMessage;
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
