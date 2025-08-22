package com.ojo.mullyuojo.hub.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HubSearchDto {
    private Long id;
    private String hubName;
    private String address;
    private String province;

    private Long companyId;
    private String companyName;
    private Long deliveryManagerId;
    private String deliveryManagerName;

}
