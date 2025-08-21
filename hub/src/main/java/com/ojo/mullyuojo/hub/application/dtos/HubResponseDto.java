package com.ojo.mullyuojo.hub.application.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ojo.mullyuojo.hub.domain.Hub;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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


    public static HubResponseDto from(Hub hub) {
        return HubResponseDto.builder()
                .id(hub.getId())
                .hubName(hub.getHubName())
                .address(hub.getAddress())
                .province(hub.getProvince())
                .latitude(hub.getLatitude())
                .longitude(hub.getLongitude())
                .build();
    }

}
