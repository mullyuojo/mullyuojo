package com.ojo.mullyuojo.delivery.domain.delivery.dto;

import com.ojo.mullyuojo.delivery.domain.delivery.status.DeliveryStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public record DeliveryUpdateRequestDto(


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
}
