package com.ojo.mullyuojo.delivery.domain.hub_delivery;

import com.ojo.mullyuojo.delivery.domain.hub_delivery.dto.HubDeliveryResponseDto;
import com.ojo.mullyuojo.delivery.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hub-delivery-channel")
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

    public ApiResponse<?> deleteHubDelivery(@PathVariable(name = "hubDeliveryId") Long hubDeliveryId) {
        hubDeliveryService.deleteHubDelivery(hubDeliveryId);
        return ApiResponse.success(200, "배송 삭제 완료");
    }

}
