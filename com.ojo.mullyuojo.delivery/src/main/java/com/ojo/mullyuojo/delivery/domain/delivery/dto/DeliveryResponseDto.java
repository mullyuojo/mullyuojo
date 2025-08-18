package com.ojo.mullyuojo.delivery.domain.delivery.dto;

import com.ojo.mullyuojo.delivery.domain.delivery.Delivery;
import com.ojo.mullyuojo.delivery.domain.delivery.status.DeliveryStatus;

public record DeliveryResponseDto(
        Long deliveryId,
        Long originHubId,
        Long destinationHubId,
        Long orderId,
        DeliveryStatus deliveryStatus,
        Long destinationCompanyId,
        Long companyManagerId,
        String companyManagerSlackId,
        Long hubDeliveryManagerId,
        Long companyDeliveryManagerId
) {
    public static DeliveryResponseDto from(
            Delivery delivery
    ){
        return new DeliveryResponseDto(
                delivery.getId(),
                delivery.getOriginHubId(),
                delivery.getDestinationHubId(),
                delivery.getOrderId(),
                delivery.getStatus(),
                delivery.getDestinationCompanyId(),
                delivery.getCompanyManagerId(),
                delivery.getCompanyManagerSlackId(),
                delivery.getHubDeliveryManagerId(),
                delivery.getCompanyDeliveryManagerId()
        );
    }
}
