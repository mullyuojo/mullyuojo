package com.ojo.mullyuojo.order.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ojo.mullyuojo.order.application.dtos.OrderRequestDto;
import com.ojo.mullyuojo.order.application.dtos.OrderResponseDto;
import com.ojo.mullyuojo.order.application.exception.BusinessException;
import com.ojo.mullyuojo.order.application.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order", indexes = { @Index(name = "idx_orders_name", columnList = "name")})
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE products SET deleted_at = now(), deleted_by = ? WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedBy
    @Column(name = "created_by", nullable = false)
    private String writer;

    @Column(nullable = false)
    private Long supplierId;

    @Column(nullable = false)
    private Long receiverId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long hubId;

    @Column(nullable = false)
    private Long deliveryId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false, length = 50)
    private String productName;

    @Column(nullable = false, precision = 15)
    private BigDecimal productQuantity;

    @Column(nullable = false, precision = 15)
    private BigDecimal productPrice;

    @Column(nullable = false, precision = 15)
    private BigDecimal totalPrice;

    @Column(length = 150)
    private String requestNotes;

    @Column(length = 150)
    private String requestDates;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "deadLine", nullable = false)
    private LocalDateTime deadLine;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Version  //낙관적 락 구현(데이터 충돌=덮어쓰기 방지)
    private Long version;

    @PrePersist
    public void setDeadline() {
        if (this.deadLine == null) {
            this.deadLine = this.createdAt.plusDays(7);
        }
    }

    // 주문 생성
    public static Order createOrder(OrderRequestDto dto, String userId) {
        requiredNonNull(userId, "writer");
        requiredNonNull(dto.getSupplierId(), "supplierId");
        requiredNonNull(dto.getReceiverId(), "receiverId");
        requiredNonNull(dto.getProductId(), "productId");
        requiredNonNull(dto.getDeliveryId(), "deliveryId");
        requiredNonNull(dto.getHubId(), "hubId");
        requiredNonBlank(dto.getProductName(), "productName");
        requiredNonNull(dto.getProductQuantity(), "productQuantity");
        requiredNonNull(dto.getProductPrice(), "productPrice");

        validateQuantity(dto.getProductQuantity());

        BigDecimal totalPrice = dto.getProductPrice().multiply(dto.getProductQuantity());

        return Order.builder()
                .writer(userId)
                .supplierId(dto.getSupplierId())
                .receiverId(dto.getReceiverId())
                .productId(dto.getProductId())
                .deliveryId(dto.getDeliveryId())
                .hubId(dto.getHubId())
                .status(OrderStatus.PENDING) // 처음엔 대기 상태라고 가정
                .productName(dto.getProductName().trim())
                .productQuantity(dto.getProductQuantity())
                .productPrice(dto.getProductPrice())
                .totalPrice(totalPrice)
                .requestNotes(dto.getRequestNotes())
                .requestDates(dto.getRequestDates())
                .build();
    }

    // 주문 삭제
    public void deleteOrder(String deletedBy){
        if(this.deletedAt != null){
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND, "이미 삭제된 주문입니다.");
        }
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

    // 주문 수정은 -> 주문 취소 요청만 가능하도록
    public void cancelOrder(){
        if(this.deletedBy != null){
            throw new BusinessException(ErrorCode.INVALID_INPUT,"이미 취소된 주문입니다.");
        }
        //취소 가능 상태만 허용
        if(!(this.status == OrderStatus.PENDING ||
                this.status == OrderStatus.PAYMENT_COMPLETE ||
                this.status == OrderStatus.ORDER_CONFIRMED)){
            throw new BusinessException(ErrorCode.INVALID_INPUT, "현재 상태에서는 주문 취소가 불가능합니다.");
        }
        this.status = OrderStatus.ORDER_CANCELED;
    }

    // 환불 요청
    public void requestFund(){
        if(this.status == OrderStatus.REFUND_REQUESTED || this.status == OrderStatus.REFUND_COMPLETE){
            throw new BusinessException(ErrorCode.INVALID_INPUT,"이미 환불이 진행중이거나 환불이 완료된 주문입니다.");
        }

        if(this.status != OrderStatus.DELIVERY_COMPLETE){
            throw new BusinessException(ErrorCode.INVALID_INPUT, " 환불 요청은 배송 완료 상태에서 가능합니다.");
        }

        this.status = OrderStatus.REFUND_REQUESTED;
    }

    // 상태 변경
    public void changeStatus(OrderStatus newStatus){
        requiredNonNull(newStatus, "status");
        this.status = newStatus;
    }

    public OrderResponseDto toOrderResponseDto() {
        return new OrderResponseDto(
                this.id,
                this.writer,
                this.supplierId,
                this.receiverId,
                this.productId,
                this.deliveryId,
                this.hubId,
                this.status,
                this.productName,
                this.productPrice,
                this.productQuantity,
                this.totalPrice,
                this.requestNotes,
                this.requestDates,

                this.createdAt,
                this.deadLine
        );
    }

    // 유효성 검사

    private static void validateQuantity(BigDecimal quantity) {
        if(quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,"요청수량은 1개 이상부터 입력 가능합니다.");
        }
        if(quantity.stripTrailingZeros().scale() > 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,"수량은 정수 단위로만 입력 가능합니다.");
        }
    }

    private static void requiredNonNull(Object o, String f) {
        if(o==null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,f+" 칸을 입력해주세요.");
        }
    }

    // String 에서만 공백 개념이 있으므로 메서드를 따로 만들어준다
    private static void requiredNonBlank(String s, String f) {
        if(s==null || s.isBlank()){
            throw new BusinessException(ErrorCode.INVALID_INPUT,f + " 칸을 입력해주세요");
        }
    }
}
