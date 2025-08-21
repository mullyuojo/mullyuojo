package com.ojo.mullyuojo.order.controller;

import com.ojo.mullyuojo.order.application.OrderService;
import com.ojo.mullyuojo.order.application.dtos.OrderRequestDto;
import com.ojo.mullyuojo.order.application.dtos.OrderResponseDto;
import com.ojo.mullyuojo.order.application.dtos.OrderSearchDto;
import com.ojo.mullyuojo.order.application.security.AccessContext;
import com.ojo.mullyuojo.order.application.security.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @ModelAttribute
    public AccessContext setAccessContext(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Role") Role role,
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-Hub-Id") Long hubId
    ) {
        return new AccessContext(userId, role, companyId, hubId);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> getOrders(
            @ModelAttribute OrderSearchDto searchDto,
            Pageable pageable){
        return ResponseEntity.ok(orderService.getOrders(searchDto, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(
            @PathVariable Long id,
            @ModelAttribute AccessContext ctx){
        return ResponseEntity.ok(orderService.getOrderById(id, ctx));
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto requestDto,
                                                        @ModelAttribute AccessContext ctx){
        return ResponseEntity.ok(orderService.createOrder(requestDto, ctx.getUserId(), ctx));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(
            @PathVariable Long id,
            @ModelAttribute AccessContext ctx){
        OrderResponseDto responseDto = orderService.cancelOrder(id, ctx);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id,
                                            @ModelAttribute AccessContext ctx){
        orderService.deleteOrder(id, ctx);
        return ResponseEntity.noContent().build();
    }
}
