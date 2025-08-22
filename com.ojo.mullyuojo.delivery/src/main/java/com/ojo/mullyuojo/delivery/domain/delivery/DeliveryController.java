package com.ojo.mullyuojo.delivery.domain.delivery;


import com.ojo.mullyuojo.delivery.domain.delivery.dto.DeliveryUpdateRequestDto;
import com.ojo.mullyuojo.delivery.domain.delivery.dto.DeliveryRequestDto;
import com.ojo.mullyuojo.delivery.domain.delivery.dto.DeliveryResponseDto;
import com.ojo.mullyuojo.delivery.utils.ApiResponse;
import com.ojo.mullyuojo.delivery.utils.QueueMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;


    @GetMapping("")
    public ApiResponse<?> getAllDelivery(Authentication auth) {
        List<DeliveryResponseDto> response = deliveryService.getAllDelivery(auth);
        return ApiResponse.success(200, response);
    }

    @GetMapping("/{deliveryId}")
    public ApiResponse<?> getDelivery(@PathVariable(name = "deliveryId") Long deliveryId, Authentication auth) {
        DeliveryResponseDto response = deliveryService.getDelivery(deliveryId, auth);
        return ApiResponse.success(200, response);
    }

    @PostMapping("")
    public ApiResponse<?> createDelivery(QueueMessage queueMessage, @RequestBody @Valid DeliveryRequestDto requestDto, Authentication auth ) {
        deliveryService.createDelivery(requestDto, auth, queueMessage);
        return ApiResponse.success(201, "배송 생성 완료");
    }

    //주문생성시 자동으로 호출 -> message queue 활용
    @RabbitListener(queues = "${message.queue.delivery}")
    public void createDeliveryByQueue(QueueMessage queueMessage) {
        System.out.println("메시지 왓나? " + queueMessage);
        deliveryService.createDeliveryByQueue(queueMessage);
        log.info("*** message queue 처리 : 배송 생성 완료");
    }

    @PatchMapping("/{deliveryId}")
    public ApiResponse<?> updateDelivery(@PathVariable(name = "deliveryId") Long deliveryId, @RequestBody DeliveryUpdateRequestDto requestDto, Authentication auth) {
        deliveryService.updateDelivery(deliveryId, requestDto, auth);
        return ApiResponse.success(200, "배송 생성 완료");
    }

    @DeleteMapping("/{deliveryId}")
    public ApiResponse<?> deleteDelivery(@PathVariable(name = "deliveryId") Long deliveryId, Authentication auth) {
        deliveryService.deleteDelivery(deliveryId, auth);
        return ApiResponse.success(204, "배송 삭제 완료");
    }


}
