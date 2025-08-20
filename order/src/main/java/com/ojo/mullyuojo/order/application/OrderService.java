package com.ojo.mullyuojo.order.application;


import com.ojo.mullyuojo.order.application.dtos.OrderRequestDto;
import com.ojo.mullyuojo.order.application.dtos.OrderResponseDto;
import com.ojo.mullyuojo.order.application.dtos.OrderSearchDto;
import com.ojo.mullyuojo.order.application.exception.BusinessException;
import com.ojo.mullyuojo.order.application.exception.ErrorCode;
import com.ojo.mullyuojo.order.application.security.AccessContext;
import com.ojo.mullyuojo.order.application.security.AccessGuard;
import com.ojo.mullyuojo.order.application.security.Action;
import com.ojo.mullyuojo.order.application.security.ResourceScope;
import com.ojo.mullyuojo.order.domain.Order;
import com.ojo.mullyuojo.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;

    // 주문 목록 검색
    public Page<OrderResponseDto> getOrders(OrderSearchDto searchDto, Pageable pageable) {
        return orderRepository.searchOrders(searchDto, pageable);
    }

    // 주문 단건 조회
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long orderId, AccessContext ctx) {
        Order order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        AccessGuard.requiredPermission(
                Action.READ,
                ctx,
                ResourceScope.of(order.getHubId(),order.getSupplierId(), order.getReceiverId())
        );
        return toResponseDto(order);
    }

    // 주문 생성
    public OrderResponseDto createOrder(OrderRequestDto requestDto,String userId, AccessContext ctx) {
        AccessGuard.requiredPermission(
                Action.CREATE,
                ctx,
                ResourceScope.of(requestDto.getHubId(), requestDto.getSupplierId(), requestDto.getReceiverId())
        );
        Order order = Order.createOrder(requestDto, userId);

        // 주문 내 상품의 업체 강제 세팅
        order.setReceiverId(ctx.getCompanyId());
        order.setSupplierId(ctx.getCompanyId()); // <-- 나중에 product의 companyId로 바꿔야함

        Order savedOrder = orderRepository.save(order);
        return toResponseDto(savedOrder);
    }

    // 주문 취소
    @Transactional
    public OrderResponseDto cancelOrder(Long orderId, AccessContext ctx) {
        Order order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        AccessGuard.requiredPermission(
                Action.UPDATE,
                ctx,
                ResourceScope.of(order.getHubId(), order.getSupplierId(), order.getReceiverId())
        );
        order.cancelOrder();
        return toResponseDto(order);
    }

    // 주문 삭제

    @Transactional
    public void deleteOrder(Long orderId, AccessContext ctx) {
        Order order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        AccessGuard.requiredPermission(
                Action.DELETE,
                ctx,
                ResourceScope.of(order.getHubId(), order.getSupplierId(), order.getReceiverId())
        );
        order.deleteOrder(ctx.getUserId());
    }



    private OrderResponseDto toResponseDto(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getWriter(),
                order.getSupplierId(),
                order.getReceiverId(),
                order.getProductId(),
                order.getDeliveryId(),
                order.getHubId(),
                order.getStatus(),
                order.getProductName(),
                order.getProductPrice(),
                order.getProductQuantity(),
                order.getTotalPrice(),
                order.getRequestNotes(),
                order.getRequestDates(),

                order.getCreatedAt(),
                order.getDeadLine()

        );
    }
}
