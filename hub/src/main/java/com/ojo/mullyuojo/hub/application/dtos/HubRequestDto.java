package com.ojo.mullyuojo.hub.application.dtos;

import com.ojo.mullyuojo.hub.application.dtos.companyListDto.CompanyDto;
import com.ojo.mullyuojo.hub.application.dtos.deliveryManagerListDto.DeliveryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class HubRequestDto {

    private String hubName;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String province;
    private String writer;

    private List<CompanyDto> companyLists;
    private List<DeliveryDto> deliveryManagerLists;

}
