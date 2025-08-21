package com.ojo.mullyuojo.slackmessage;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;

import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SlackMessageBotService {

    private String API_KEY = "xoxb-9346624036995-9369196641863-xXsGA9Hvv0p3gi10HYShClvq";
    private String CHANNEL_ID = "C09AV6372S3";

    Slack slack = Slack.getInstance();
    String botToken ="xoxb-9346624036995-9369196641863-xXsGA9Hvv0p3gi10HYShClvq";
    String botToken2 = "xoxb-9380129097203-9385345045154-KVNF8THKQLK8fuPAGjDVgefr"; // my testspace
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
