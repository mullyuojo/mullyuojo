<<<<<<< HEAD
package com.ojo.mullyuojo.slackmessage.domain.slackmessage;

import com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto.SlackRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "p_slackmessage")
@Getter
@Setter
@NoArgsConstructor
public class SlackMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    private String recipient;
    private String message;
    private LocalDateTime createdAt;
    private boolean issended;

    private long deleted_by;
    private LocalDateTime deleted_at;

    public SlackMessage(SlackRequestDto slackRequestDto) {
        this.sender = slackRequestDto.getSender();
        this.recipient = slackRequestDto.getRecipient();
        this.message = slackRequestDto.getMessage();
        this.createdAt = LocalDateTime.now();
    }

    public void update(SlackRequestDto requestDto) {
        this.sender = requestDto.getSender();
        this.recipient = requestDto.getRecipient();
        this.message = requestDto.getMessage();
    }
}
=======
package com.ojo.mullyuojo.slackmessage.domain.slackmessage;

import com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto.SlackRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "p_slackmessage")
@Getter
@Setter
@NoArgsConstructor
public class SlackMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    private String recipient;
    private String message;
    private LocalDateTime createdAt;
    private boolean issended;

    private long deleted_by;
    private LocalDateTime deleted_at;

    public SlackMessage(SlackRequestDto slackRequestDto) {
        this.sender = slackRequestDto.getSender();
        this.recipient = slackRequestDto.getRecipient();
        this.message = slackRequestDto.getMessage();
        this.createdAt = LocalDateTime.now();
    }

    public void update(SlackRequestDto requestDto) {
        this.sender = requestDto.getSender();
        this.recipient = requestDto.getRecipient();
        this.message = requestDto.getMessage();
    }
}
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
