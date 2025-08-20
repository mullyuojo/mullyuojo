package com.ojo.mullyuojo.delivery.domain.delivery;


import com.ojo.mullyuojo.delivery.domain.delivery.dto.DeliveryUpdateRequestDto;
import com.ojo.mullyuojo.delivery.domain.delivery.dto.DeliveryRequestDto;
import com.ojo.mullyuojo.delivery.domain.delivery.dto.DeliveryResponseDto;
import com.ojo.mullyuojo.delivery.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;


    @GetMapping("/{userId}")
    public ApiResponse<?> getAllDelivery(@PathVariable(name = "userId") Long userId){
        List<DeliveryResponseDto> response = deliveryService.getAllDelivery(userId);
        return ApiResponse.success(200, response);
    }
    @GetMapping("/{deliveryId}/{userId}")
    public ApiResponse<?> getDelivery(@PathVariable(name = "deliveryId") Long deliveryId, @PathVariable(name = "userId") Long userId){
        DeliveryResponseDto response = deliveryService.getDelivery(deliveryId, userId);
        return ApiResponse.success(200, response);
    }

    //주문생성시 자동으로 호출 -> message queue 활용
    @PostMapping("/{userId}")
    public ApiResponse<?> createDelivery(@RequestBody @Valid DeliveryRequestDto requestDto, @PathVariable(name = "userId") Long userId){
        deliveryService.createDelivery(requestDto, userId);
        return ApiResponse.success(201, "배송 생성 완료");
    }

    @PatchMapping("/{deliveryId}/{userId}")
    public ApiResponse<?> updateDelivery(@PathVariable(name = "deliveryId") Long deliveryId, @RequestBody DeliveryUpdateRequestDto requestDto, @PathVariable(name = "userId") Long userId){
        deliveryService.updateDelivery(deliveryId, requestDto, userId);
        return ApiResponse.success(200, "배송 생성 완료");
    }

    @DeleteMapping("/{deliveryId}/{userId}")
    public ApiResponse<?> deleteDelivery(@PathVariable(name = "deliveryId") Long deliveryId, @PathVariable(name = "userId") Long userId){
        deliveryService.deleteDelivery(deliveryId, userId);
        return ApiResponse.success(204, "배송 삭제 완료");
    }


}
