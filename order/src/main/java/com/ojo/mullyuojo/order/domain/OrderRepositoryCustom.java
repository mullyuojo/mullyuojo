package com.ojo.mullyuojo.order.domain;

import com.ojo.mullyuojo.order.application.dtos.OrderResponseDto;
import com.ojo.mullyuojo.order.application.dtos.OrderSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<OrderResponseDto> searchOrders(OrderSearchDto searchDto, Pageable pageable);
}
