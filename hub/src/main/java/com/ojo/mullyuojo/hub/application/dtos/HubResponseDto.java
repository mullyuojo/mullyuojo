package com.ojo.mullyuojo.hub.application.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ojo.mullyuojo.hub.application.dtos.companyListDto.CompanyDto;
import com.ojo.mullyuojo.hub.application.dtos.deliveryManagerListDto.DeliveryDto;
import com.ojo.mullyuojo.hub.domain.CompanyList;
import com.ojo.mullyuojo.hub.domain.DeliveryManagerList;
import com.ojo.mullyuojo.hub.domain.Hub;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HubResponseDto {

    private Long id;
    private String hubName;
    private String address;
    private String province;
    private BigDecimal latitude;
    private BigDecimal longitude;

    private List<CompanyDto> companyLists;
    private List<DeliveryDto> deliveryManagerLists;


    public static HubResponseDto from(Hub hub) {
        return HubResponseDto.builder()
                .id(hub.getId())
                .hubName(hub.getHubName())
                .address(hub.getAddress())
                .province(hub.getProvince())
                .latitude(hub.getLatitude())
                .longitude(hub.getLongitude())
                .companyLists(hub.getCompanyLists() != null
                ? hub.getCompanyLists().stream()
                        .map(c-> new CompanyDto(c.getId(), c.getName()))
                        .toList()
                        :new ArrayList<>()
                )
                .deliveryManagerLists(hub.getDeliveryManagerLists() != null
                ? hub.getDeliveryManagerLists().stream()
                        .map(d -> new DeliveryDto(d.getId(), d.getName()))
                        .toList()
                        :new ArrayList<>()
                )
                .build();
    }

}
