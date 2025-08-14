package com.ojo.mullyuojo.delivery.domain.delivery.dto;

import com.ojo.mullyuojo.delivery.domain.delivery.Delivery;
import com.ojo.mullyuojo.delivery.domain.delivery.status.DeliveryStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public record DeliveryResponseDto(
        Long deliveryId,
        Long originHubId,
        Long destinationHubId,
        Long orderId,
        DeliveryStatus deliveryStatus,
        String companyAddress,
        Long companyManagerId,
        String companyManagerSlackId,
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
                delivery.getCompanyAddress(),
                delivery.getCompanyManagerId(),
                delivery.getCompanyManagerSlackId(),
                delivery.getCompanyDeliveryManagerId()
        );
    }
}
