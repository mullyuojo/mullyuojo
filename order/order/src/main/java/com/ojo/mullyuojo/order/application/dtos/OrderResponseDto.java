package com.ojo.mullyuojo.order.application.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ojo.mullyuojo.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseDto {

    private Long id;
    private String writer;
    private Long supplierId;
    private Long receiverId;
    private Long productId;
    private Long deliveryId;
    private Long hubId;
    private OrderStatus status;

    private String productName;
    private BigDecimal productPrice;
    private BigDecimal productQuantity;

    private BigDecimal totalPrice;
    private String requestNotes;
    private String requestDates;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deadLine;
}
