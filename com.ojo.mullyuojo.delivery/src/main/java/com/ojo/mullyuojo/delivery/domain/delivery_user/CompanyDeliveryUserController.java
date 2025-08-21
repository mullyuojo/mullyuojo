package com.ojo.mullyuojo.delivery.domain.delivery_user;

import com.ojo.mullyuojo.delivery.domain.delivery_user.dto.CompanyDeliveryUserRequestDto;
import com.ojo.mullyuojo.delivery.domain.delivery_user.dto.CompanyDeliveryUserResponseDto;
import com.ojo.mullyuojo.delivery.domain.delivery_user.dto.CompanyDeliveryUserUpdateRequestDto;
import com.ojo.mullyuojo.delivery.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery-users")
@RequiredArgsConstructor
public class CompanyDeliveryUserController {

    private final CompanyDeliveryUserService companyDeliveryUserService;

    @GetMapping()
    public ApiResponse<?> getAllUser() {
        List<CompanyDeliveryUserResponseDto> response = companyDeliveryUserService.getAllUser();
        return ApiResponse.success(200, response);
    }

    @GetMapping("/{deliveryUserId}")
    public ApiResponse<?> getUser(@PathVariable(name = "deliveryUserId") Long deliveryUserId) {
        CompanyDeliveryUserResponseDto response = companyDeliveryUserService.getUser(deliveryUserId);
        return ApiResponse.success(200, response);
    }

    @PostMapping()
    public ApiResponse<?> create(@RequestBody @Valid CompanyDeliveryUserRequestDto requestDto) {
        CompanyDeliveryUserResponseDto response = companyDeliveryUserService.create(requestDto);
        return ApiResponse.success(201, "배송 담당자 지정 완료", response);
    }

    @PatchMapping("/{deliveryUserId}")
    public ApiResponse<?> updateUser(@RequestBody CompanyDeliveryUserUpdateRequestDto requestDto, @PathVariable(name = "deliveryUserId") Long deliveryUserId) {
        CompanyDeliveryUserResponseDto response = companyDeliveryUserService.update(deliveryUserId, requestDto);
        return ApiResponse.success(200, response);
    }

    @DeleteMapping("/{deliveryUserId}")
    public ApiResponse<?> delete(@PathVariable(name = "deliveryUserId") Long deliveryUserId) {
        companyDeliveryUserService.delete(deliveryUserId);
        return ApiResponse.success(204, "배송 담당자 삭제 완료");
    }

}
