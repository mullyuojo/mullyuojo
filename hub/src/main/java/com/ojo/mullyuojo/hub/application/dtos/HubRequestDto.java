package com.ojo.mullyuojo.hub.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

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

}
