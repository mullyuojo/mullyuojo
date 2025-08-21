package com.ojo.mullyuojo.delivery.domain.com_delivery.dto;

import com.ojo.mullyuojo.delivery.domain.com_delivery.CompanyDelivery;
import com.ojo.mullyuojo.delivery.domain.com_delivery.status.CompanyDeliveryStatus;

public record CompanyDeliveryResponseDto(
        Long id,
        Long deliveryId,
        CompanyDeliveryStatus status,
        Long originHubId,
        Long destinationCompanyId,
        Double estimatedDistance,
        Double estimatedTime,
        Double actualDistance,
        Double actualTime,
        Long companyDeliveryManagerId
) {
    public static CompanyDeliveryResponseDto from(CompanyDelivery companyDelivery) {
        return new CompanyDeliveryResponseDto(
                companyDelivery.getId(),
                companyDelivery.getDeliveryId(),
                companyDelivery.getStatus(),
                companyDelivery.getOriginHubId(),
                companyDelivery.getDestinationCompanyId(),
                companyDelivery.getEstimatedDistance(),
                companyDelivery.getEstimatedTime(),
                companyDelivery.getActualDistance(),
                companyDelivery.getActualTime(),
                companyDelivery.getCompanyDeliveryManagerId()
        );
    }
}
