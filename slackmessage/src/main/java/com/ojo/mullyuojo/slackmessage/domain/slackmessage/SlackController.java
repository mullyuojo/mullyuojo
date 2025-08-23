<<<<<<< HEAD
package com.ojo.mullyuojo.slackmessage.domain.slackmessage;

import com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto.SlackRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RequiredArgsConstructor
public class SlackController {


    private final SlackMessageService slackMessageService;



    @PostMapping("/slack")
    public ResponseEntity<?> sendMessage(@RequestBody SlackRequestDto slackRequestDto) {
        return ResponseEntity.ok(slackMessageService.sendMessage(slackRequestDto));
    }


}
=======
package com.ojo.mullyuojo.slackmessage.domain.slackmessage;

import com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto.SlackRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RequiredArgsConstructor
public class SlackController {


    private final SlackMessageService slackMessageService;



    @PostMapping("/slack")
    public ResponseEntity<?> sendMessage(@RequestBody SlackRequestDto slackRequestDto) {
        return ResponseEntity.ok(slackMessageService.sendMessage(slackRequestDto));
    }


}
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
