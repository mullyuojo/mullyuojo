package com.ojo.mullyuojo.slackmessage.domain.slackmessage.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDeliveryResponseDto {
    Long id;
    Long deliveryId;
    CompanyDeliveryStatus status;
    Long originHubId;
    Long destinationCompanyId;
    Double estimatedDistance;
    Double estimatedTime;
    Double actualDistance;
    Double actualTime;
    Long companyDeliveryManagerId;

    public enum CompanyDeliveryStatus {
        OUT_FOR_DELIVERY,
        IN_TRANSIT_TO_COMPANY,
        DELIVERED
    }

}
