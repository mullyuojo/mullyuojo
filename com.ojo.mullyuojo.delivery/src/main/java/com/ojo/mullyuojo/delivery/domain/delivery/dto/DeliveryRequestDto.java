package com.ojo.mullyuojo.delivery.domain.delivery.dto;

import com.ojo.mullyuojo.delivery.domain.delivery.status.DeliveryStatus;
import jakarta.validation.constraints.NotNull;

public record DeliveryRequestDto(
        @NotNull
        Long originHubId,
        @NotNull
        Long destinationHubId,
        @NotNull
        Long orderId,
        DeliveryStatus deliveryStatus,
        @NotNull
        Long destinationCompanyId,
        @NotNull
        Long companyManagerId,
        @NotNull
        String companyManagerSlackId,
        @NotNull
        Long hubDeliveryManagerId

) {
}
