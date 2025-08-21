package com.ojo.mullyuojo.delivery.domain.delivery.client.hub;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class DeliveryHubDto {

    Long id;
    String name;
    String address;
    Double latitude;
    Double longitude;
    List<Long> companyList;
    List<Long> managerList;
    Long manager;

}
