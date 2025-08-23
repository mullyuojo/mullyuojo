<<<<<<< HEAD
package com.ojo.mullyuojo.slackmessage.domain.slackmessage;

import com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto.SlackRequestDto;
import com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto.SlackResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slacks")
@RequiredArgsConstructor
public class SlackMessageController {

    private final SlackMessageService slackMessageService;

    @PostMapping()
    public ResponseEntity<?> sendMessage(@RequestBody SlackRequestDto slackRequestDto) {
        return ResponseEntity.ok(slackMessageService.sendMessage(slackRequestDto));
    }
    @PostMapping("/{id}")
    public ResponseEntity<?> sendExistingMessage(@PathVariable Long id) {
        return ResponseEntity.ok(slackMessageService.sendExistingMessage(id));
    }


    @PostMapping("/create")
    public ResponseEntity<SlackResponseDto> createMessage(@RequestBody SlackRequestDto requestDto) {
        SlackResponseDto responseDto = slackMessageService.createMessage(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    //@PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<List<SlackResponseDto>> getMessages() {
        List<SlackResponseDto> messages = slackMessageService.getMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<SlackResponseDto> getMessageById(@PathVariable Long id) {
        // SlackService에 단일 메시지 조회 메소드가 없으므로, getMessages()를 활용하거나 Service에 추가해야 합니다.
        // 여기서는 예시를 위해 간단히 처리합니다. 실제로는 Service에 findById 메소드를 추가하는 것이 좋습니다.
        List<SlackResponseDto> messages = slackMessageService.getMessages();
        return messages.stream()
                .filter(msg -> msg.getSender().equals(String.valueOf(id))) // 임시로 id를 sender로 매핑
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<SlackResponseDto> updateMessage(@PathVariable Long id, @RequestBody SlackRequestDto requestDto) {
        SlackResponseDto responseDto = slackMessageService.updateMessage(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        slackMessageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
=======
package com.ojo.mullyuojo.slackmessage.domain.slackmessage;

import com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto.SlackRequestDto;
import com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto.SlackResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slacks")
@RequiredArgsConstructor
public class SlackMessageController {

    private final SlackMessageService slackMessageService;

    @PostMapping()
    public ResponseEntity<?> sendMessage(@RequestBody SlackRequestDto slackRequestDto) {
        return ResponseEntity.ok(slackMessageService.sendMessage(slackRequestDto));
    }
    @PostMapping("/{id}")
    public ResponseEntity<?> sendExistingMessage(@PathVariable Long id) {
        return ResponseEntity.ok(slackMessageService.sendExistingMessage(id));
    }


    @PostMapping("/create")
    public ResponseEntity<SlackResponseDto> createMessage(@RequestBody SlackRequestDto requestDto) {
        SlackResponseDto responseDto = slackMessageService.createMessage(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    //@PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<List<SlackResponseDto>> getMessages() {
        List<SlackResponseDto> messages = slackMessageService.getMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<SlackResponseDto> getMessageById(@PathVariable Long id) {
        // SlackService에 단일 메시지 조회 메소드가 없으므로, getMessages()를 활용하거나 Service에 추가해야 합니다.
        // 여기서는 예시를 위해 간단히 처리합니다. 실제로는 Service에 findById 메소드를 추가하는 것이 좋습니다.
        List<SlackResponseDto> messages = slackMessageService.getMessages();
        return messages.stream()
                .filter(msg -> msg.getSender().equals(String.valueOf(id))) // 임시로 id를 sender로 매핑
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<SlackResponseDto> updateMessage(@PathVariable Long id, @RequestBody SlackRequestDto requestDto) {
        SlackResponseDto responseDto = slackMessageService.updateMessage(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        slackMessageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
