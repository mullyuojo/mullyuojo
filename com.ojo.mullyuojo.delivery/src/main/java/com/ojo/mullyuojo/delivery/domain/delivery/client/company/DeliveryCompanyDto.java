package com.ojo.mullyuojo.delivery.domain.delivery.client.company;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DeliveryCompanyDto {

    Long id;
    Long name;
    Type type;
    Long hubId;
    List<Long> productList;
    String address;
    List<Long> managers;

    public enum Type{SUPPLIER, RECEIVER}
}
