package com.ojo.mullyuojo.order.domain;

import com.ojo.mullyuojo.order.application.dtos.OrderResponseDto;
import com.ojo.mullyuojo.order.application.dtos.OrderSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.ojo.mullyuojo.order.domain.QOrder.order;


@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderResponseDto> searchOrders(OrderSearchDto searchDto, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        //ID 조회
        if(searchDto.getId() != null){
            builder.and(order.id.eq(searchDto.getId()));
        }
        if(searchDto.getSupplierId() != null){
            builder.and(order.supplierId.eq(searchDto.getSupplierId()));
        }
        if(searchDto.getReceiverId() != null){
            builder.and(order.receiverId.eq(searchDto.getReceiverId()));
        }
        if(searchDto.getProductId() != null){
            builder.and(order.productId.eq(searchDto.getProductId()));
        }
        if(searchDto.getDeliveryId() != null){
            builder.and(order.deliveryId.eq(searchDto.getDeliveryId()));
        }
        if(searchDto.getHubId() != null){
            builder.and(order.hubId.eq(searchDto.getHubId()));
        }

        // 복잡한조건
        builder.and(writerContains(searchDto.getWriter()));
        builder.and(productNameContains(searchDto.getProductName()));
        builder.and(StatusEq(searchDto.getStatus()));
        builder.and(orderDateBetween(searchDto.getCreatedFrom(), searchDto.getCreatedTo()));
        builder.and(deadLineBetween(searchDto.getDeadlineFrom(), searchDto.getDeadlineTo()));

        // 실제 조회
        List<OrderResponseDto> orders = queryFactory
                .select(Projections.constructor(OrderResponseDto.class,
                        order.id,
                        order.writer,
                        order.supplierId,
                        order.receiverId,
                        order.productId,
                        order.deliveryId,
                        order.hubId,
                        order.status,
                        order.productName,
                        order.productQuantity,
                        order.productPrice,
                        order.totalPrice,
                        order.requestNotes,
                        order.requestDates,
                        order.createdAt,
                        order.deadLine
                ))
                .from(order)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        //전체 카운트
        Long total = queryFactory
                .select(order.count())
                .from(order)
                .where(builder)
                .fetchOne();

        long totalCount = (total != null) ? total : 0L;

        return new PageImpl<>(orders, pageable, totalCount);
    }

    private BooleanExpression writerContains(String writer){
        return writer != null ? order.writer.containsIgnoreCase(writer) : null;
    }

    private BooleanExpression productNameContains(String name){
        return name != null ? order.productName.containsIgnoreCase(name) : null;
    }

    private BooleanExpression StatusEq(OrderStatus status){
        return status != null ? order.status.eq(status) : null;
    }

    private BooleanExpression orderDateBetween(LocalDateTime start, LocalDateTime end) {
        if(start != null && end != null){
            return order.createdAt.between(start, end);
        }else if (start != null){
            return order.createdAt.goe(start);
        }else if (end != null){
            return order.createdAt.loe(end);
        }else {
            return null;
        }
    }

    private BooleanExpression deadLineBetween(LocalDateTime start, LocalDateTime end) {
        if(start != null && end != null){
            return order.deadLine.between(start, end);
        }else if (start != null){
            return order.deadLine.goe(start);
        }else if (end != null){
            return order.deadLine.loe(end);
        }else {
            return null;
        }
    }

}
