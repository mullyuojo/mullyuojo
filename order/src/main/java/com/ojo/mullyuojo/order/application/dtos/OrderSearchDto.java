package com.ojo.mullyuojo.order.application.dtos;

import com.ojo.mullyuojo.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class OrderSearchDto {
    private Long id;
    private String writer;
    private Long supplierId;
    private Long receiverId;
    private Long productId;
    private Long deliveryId;
    private Long hubId;

    private OrderStatus status;

    private String productName;

    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;

    private LocalDateTime deadlineFrom;
    private LocalDateTime deadlineTo;


}
