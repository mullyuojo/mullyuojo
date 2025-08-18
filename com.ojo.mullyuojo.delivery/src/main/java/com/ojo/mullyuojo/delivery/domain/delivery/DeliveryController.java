package com.ojo.mullyuojo.delivery.domain.delivery;


import com.ojo.mullyuojo.delivery.domain.com_delivery.CompanyDeliveryService;
import com.ojo.mullyuojo.delivery.domain.delivery.dto.DeliveryRequestDto;
import com.ojo.mullyuojo.delivery.domain.delivery.dto.DeliveryResponseDto;
import com.ojo.mullyuojo.delivery.domain.hub_delivery.HubDeliveryService;
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


    @GetMapping()
    public ApiResponse<?> getAllDelivery(){
        List<DeliveryResponseDto> response = deliveryService.getAllDelivery();
        return ApiResponse.success(200, response);
    }

    //주문생성시 자동으로 호출 -> message queue 활용
    @PostMapping()
    public ApiResponse<?> createDelivery(@RequestBody @Valid DeliveryRequestDto requestDto){
        deliveryService.createDelivery(requestDto);
        return ApiResponse.success(201, "배송 생성 완료");
    }

    @DeleteMapping("/{delivery_id}")
    public ApiResponse<?> deleteDelivery(@PathVariable(name = "delivery_id") Long delivery_id){
        deliveryService.deleteDelivery(delivery_id);
        return ApiResponse.success(200, "배송 삭제 완료");
    }


}
