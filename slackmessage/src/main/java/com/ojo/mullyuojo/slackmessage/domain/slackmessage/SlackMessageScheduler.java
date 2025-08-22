package com.ojo.mullyuojo.slackmessage.domain.slackmessage;

import com.ojo.mullyuojo.slackmessage.domain.slackmessage.client.CompanyDeliveryClient;
import com.ojo.mullyuojo.slackmessage.domain.slackmessage.client.CompanyDeliveryResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SlackMessageScheduler {
    private final SlackMessageBotService slackMessageBotService;
    private final SlackRepository slackRepository;
    private final SlackMessageService slackMessageService;
    private final CompanyDeliveryClient companyDeliveryClient;


    @Scheduled(cron = "40 09 19 * * *")
    public void sendMessageAt6am(){
        log.info("Sending Message at6am");

        CompanyDeliveryResponseDto dto = companyDeliveryClient.getCompanyDelivery(5L);
        StringBuilder sb = new StringBuilder();
        sb.append("deliveryId : ").append(dto.getDeliveryId()).append("\n");
        sb.append("status : ").append(dto.getStatus()).append("\n");
        sb.append("originHubId : ").append(dto.getOriginHubId()).append("\n");
        sb.append("destinationCompanyId: ").append(dto.getDestinationCompanyId()).append("\n");
        sb.append("estimatedDistance : ").append(dto.getEstimatedDistance()).append("\n");
        sb.append("estimatedTime : ").append(dto.getEstimatedTime()).append("\n");
        sb.append("actualDistance : ").append(dto.getActualDistance()).append("\n");
        sb.append("actualTime : ").append(dto.getActualTime()).append("\n");
        sb.append("companyDeliveryManagerId : ").append(dto.getCompanyDeliveryManagerId()).append("\n");


        List<SlackMessage> slackMessages = slackRepository.findAll().stream().toList();

        for (SlackMessage message: slackMessages){
            String combinedmessage = slackMessageService.MesaageCombinder(message);
            String recipient = message.getRecipient();
            slackMessageBotService.sendMessage(sb.toString(),"U090UQQF8V7");
        }
    }


}
