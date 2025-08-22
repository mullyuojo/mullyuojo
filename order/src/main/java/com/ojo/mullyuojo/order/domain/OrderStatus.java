package com.ojo.mullyuojo.order.domain;

public enum OrderStatus {
    PENDING,              // 주문 대기
    PAYMENT_COMPLETE,     // 결제 완료
    ORDER_CONFIRMED,      // 주문 확정
    ORDER_CANCELED,       // 주문 취소
    SHIPPING_STARTED,     // 배송 시작
    IN_TRANSIT,           // 배송 중
    DELIVERY_COMPLETE,    // 배송 완료
    REFUND_REQUESTED,      // 환불 요청됨
    REFUND_COMPLETE       // 환불 완료
}
