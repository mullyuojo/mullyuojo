package com.ojo.mullyuojo.delivery.utils;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QueueMessage {

    private UUID orderIdQ;   //application process 상에서 기록

    private Long orderId;  //실제로 order 주문할 떄 받을 데이터
    private Long supplyCompanyId;
    private Long receiveCompanyId;
    private String errorType;   //application process 상에서 기록

}