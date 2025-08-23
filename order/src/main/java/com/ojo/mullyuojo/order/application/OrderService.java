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
import com.ojo.mullyuojo.order.utils.QueueMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${message.queue.delivery}")
    private String deliveryQueue;

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
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto,String userId, AccessContext ctx) {
        AccessGuard.requiredPermission(
                Action.CREATE,
                ctx,
                ResourceScope.of(requestDto.getHubId(), requestDto.getSupplierId(), requestDto.getReceiverId())
        );
        Order order = Order.createOrder(requestDto, userId);

        Order savedOrder = orderRepository.save(order);

        QueueMessage queueMessage = new QueueMessage(UUID.randomUUID(), order.getId(), order.getSupplierId(), order.getReceiverId());
        rabbitTemplate.convertAndSend(deliveryQueue, queueMessage);

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
