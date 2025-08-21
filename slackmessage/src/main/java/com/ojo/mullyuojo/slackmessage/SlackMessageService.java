package com.ojo.mullyuojo.slackmessage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public SlackResponseDto updateMessage(Long id, SlackRequestDto requestDto) {
        SlackMessage slackMessage = slackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid message Id:" + id));
        slackMessage.update(requestDto);
        return new SlackResponseDto(slackMessage);
    }

    @Transactional
    public void deleteMessage(Long id) {
        slackRepository.deleteById(id);
    }
}

