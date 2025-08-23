<<<<<<< HEAD
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
=======
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
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
