package com.ojo.mullyuojo.order.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class OrderRequestDto {

    private Long supplierId;
    private Long receiverId;
    private Long productId;
    private Long deliveryId;
    private Long hubId;

    private String productName;
    private BigDecimal productPrice;
    private BigDecimal productQuantity;
    private BigDecimal totalPrice;

    private String requestNotes;
    private String requestDates;
    private String deadLine;
}
