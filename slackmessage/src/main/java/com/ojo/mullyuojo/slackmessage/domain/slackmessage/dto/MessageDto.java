package com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageDto {
    String message;

    public MessageDto(String message) {
        this.message = message;
    }
}
