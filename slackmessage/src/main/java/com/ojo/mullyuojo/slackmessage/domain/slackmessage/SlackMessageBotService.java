package com.ojo.mullyuojo.slackmessage.domain.slackmessage;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;

import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SlackMessageBotService {

    Slack slack = Slack.getInstance();

    @Value("${slack.bot.token}")
    String botToken2;

    MethodsClient methods = slack.methods(botToken2);

    public void sendMessage(String message,String recipient){
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(recipient)
                .text(message)
                .build();
        try{
            ChatPostMessageResponse response = methods.chatPostMessage(request);
            if(!response.isOk()){
                throw new RuntimeException("메세지 전송 실패");
            }
        } catch (IOException | SlackApiException e) {
            e.printStackTrace();
        }
    }



}
