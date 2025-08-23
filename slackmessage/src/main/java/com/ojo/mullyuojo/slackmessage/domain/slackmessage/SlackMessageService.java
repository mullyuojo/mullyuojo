<<<<<<< HEAD
package com.ojo.mullyuojo.slackmessage.domain.slackmessage;

import com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto.MessageDto;
import com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto.SlackRequestDto;
import com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto.SlackResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SlackMessageService {
    private final SlackMessageBotService slackMessageBotService;
    private final SlackRepository slackRepository;

    @Transactional
    public SlackResponseDto sendMessage(SlackRequestDto slackRequestDto) {

        SlackMessage slackMessage = new SlackMessage(slackRequestDto);
        String message = MesaageCombinder(slackMessage);
        slackMessageBotService.sendMessage(message,slackRequestDto.getRecipient());
        slackMessage.setIssended(true);

        return new SlackResponseDto(slackMessage);
    }

    public SlackResponseDto sendExistingMessage(Long id) {
        SlackMessage slackMessage = slackRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        String message = MesaageCombinder(slackMessage);
        slackMessageBotService.sendMessage(message,slackMessage.getRecipient());
        slackMessage.setIssended(true);

        return new SlackResponseDto(slackMessage);
    }

    public String MesaageCombinder(SlackMessage slackMessage){
        slackMessage.getMessage();
        String message = "From : " + slackMessage.getSender()+ "\n"+ "Message : " + slackMessage.getMessage();
        return message;
    }

    // --- CRUD Methods for SlackMessage Data ---

    @Transactional
    public SlackResponseDto createMessage(SlackRequestDto requestDto) {
        SlackMessage slackMessage = new SlackMessage(requestDto);
        SlackMessage savedMessage = slackRepository.save(slackMessage);
        return new SlackResponseDto(savedMessage);
    }

    @Transactional(readOnly = true)
    public List<SlackResponseDto> getMessages() {
        return slackRepository.findAll().stream()
                .map(SlackResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SlackResponseDto getMessages(Long id) {
        SlackMessage slackMessage = slackRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());

        return new SlackResponseDto(slackMessage);
    }

    @Transactional
    public SlackResponseDto updateMessage(Long id, SlackRequestDto requestDto) {
        SlackMessage slackMessage = slackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid message Id:" + id));
        slackMessage.update(requestDto);
        return new SlackResponseDto(slackMessage);
    }

    @Transactional
    public MessageDto deleteMessage(Long id) {
        SlackMessage slackMessage = slackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid message Id:" + id));
        slackMessage.setDeleted_at(LocalDateTime.now());;
        String user_id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        slackMessage.setDeleted_by(Long.parseLong(user_id));
        return new MessageDto("삭제가 완료되었습니다.");
    }


}

=======
package com.ojo.mullyuojo.slackmessage.domain.slackmessage;

import com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto.MessageDto;
import com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto.SlackRequestDto;
import com.ojo.mullyuojo.slackmessage.domain.slackmessage.dto.SlackResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SlackMessageService {
    private final SlackMessageBotService slackMessageBotService;
    private final SlackRepository slackRepository;

    @Transactional
    public SlackResponseDto sendMessage(SlackRequestDto slackRequestDto) {

        SlackMessage slackMessage = new SlackMessage(slackRequestDto);
        String message = MesaageCombinder(slackMessage);
        slackMessageBotService.sendMessage(message,slackRequestDto.getRecipient());
        slackMessage.setIssended(true);

        return new SlackResponseDto(slackMessage);
    }

    public SlackResponseDto sendExistingMessage(Long id) {
        SlackMessage slackMessage = slackRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        String message = MesaageCombinder(slackMessage);
        slackMessageBotService.sendMessage(message,slackMessage.getRecipient());
        slackMessage.setIssended(true);

        return new SlackResponseDto(slackMessage);
    }

    public String MesaageCombinder(SlackMessage slackMessage){
        slackMessage.getMessage();
        String message = "From : " + slackMessage.getSender()+ "\n"+ "Message : " + slackMessage.getMessage();
        return message;
    }

    // --- CRUD Methods for SlackMessage Data ---

    @Transactional
    public SlackResponseDto createMessage(SlackRequestDto requestDto) {
        SlackMessage slackMessage = new SlackMessage(requestDto);
        SlackMessage savedMessage = slackRepository.save(slackMessage);
        return new SlackResponseDto(savedMessage);
    }

    @Transactional(readOnly = true)
    public List<SlackResponseDto> getMessages() {
        return slackRepository.findAll().stream()
                .map(SlackResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SlackResponseDto getMessages(Long id) {
        SlackMessage slackMessage = slackRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());

        return new SlackResponseDto(slackMessage);
    }

    @Transactional
    public SlackResponseDto updateMessage(Long id, SlackRequestDto requestDto) {
        SlackMessage slackMessage = slackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid message Id:" + id));
        slackMessage.update(requestDto);
        return new SlackResponseDto(slackMessage);
    }

    @Transactional
    public MessageDto deleteMessage(Long id) {
        SlackMessage slackMessage = slackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid message Id:" + id));
        slackMessage.setDeleted_at(LocalDateTime.now());;
        String user_id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        slackMessage.setDeleted_by(Long.parseLong(user_id));
        return new MessageDto("삭제가 완료되었습니다.");
    }


}

>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
