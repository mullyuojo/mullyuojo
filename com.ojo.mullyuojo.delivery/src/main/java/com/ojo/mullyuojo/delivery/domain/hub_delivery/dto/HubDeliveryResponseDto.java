package com.ojo.mullyuojo.delivery.domain.hub_delivery.dto;

import com.ojo.mullyuojo.delivery.domain.hub_delivery.HubDelivery;
import com.ojo.mullyuojo.delivery.domain.hub_delivery.status.HubDeliveryStatus;

public record HubDeliveryResponseDto(
        Long id,
        Long deliveryId,
        HubDeliveryStatus status,
        Long originHubId,
        Long destinationHubId,
        Double estimatedDistance,
        Double estimatedTime,
        Double actualDistance,
        Double actualTime,
        Long HubDeliveryManagerId
) {
    public static HubDeliveryResponseDto from(HubDelivery hubDelivery) {
        return new HubDeliveryResponseDto(
                hubDelivery.getId(),
                hubDelivery.getDeliveryId(),
                hubDelivery.getStatus(),
                hubDelivery.getOriginHubId(),
                hubDelivery.getDestinationHubId(),
                hubDelivery.getEstimatedDistance(),
                hubDelivery.getEstimatedTime(),
                hubDelivery.getActualDistance(),
                hubDelivery.getActualTime(),
                hubDelivery.getHubDeliveryManagerId()
        );
    }
}
