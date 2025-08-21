package com.ojo.mullyuojo.delivery.domain.hub_delivery;

import com.ojo.mullyuojo.delivery.domain.hub_delivery.dto.HubDeliveryResponseDto;
import com.ojo.mullyuojo.delivery.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hub-delivery-channels")
public class HubDeliveryController {

    private final HubDeliveryService hubDeliveryService;

    @GetMapping()
    public ApiResponse<?> getAllHubDelivery() {
        List<HubDeliveryResponseDto> response = hubDeliveryService.getAllHubDelivery();
        return ApiResponse.success(200, response);
    }

    @GetMapping("/{hubDeliveryId}")
    public ApiResponse<?> getHubDelivery(@PathVariable(name = "hubDeliveryId") Long hubDeliveryId) {
        HubDeliveryResponseDto response = hubDeliveryService.getHubDelivery(hubDeliveryId);
        return ApiResponse.success(200, response);
    }

    @DeleteMapping("/{hubDeliveryId}")
    public ApiResponse<?> deleteHubDelivery(@PathVariable(name = "hubDeliveryId") Long hubDeliveryId) {
        hubDeliveryService.deleteHubDelivery(hubDeliveryId);
        return ApiResponse.success(204, "배송 삭제 완료");
    }

}
