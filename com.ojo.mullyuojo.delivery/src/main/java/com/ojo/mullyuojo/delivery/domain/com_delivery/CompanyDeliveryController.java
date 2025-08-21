package com.ojo.mullyuojo.delivery.domain.com_delivery;

import com.ojo.mullyuojo.delivery.domain.com_delivery.dto.CompanyDeliveryResponseDto;
import com.ojo.mullyuojo.delivery.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/com-delivery-channels")
public class CompanyDeliveryController {

    private final CompanyDeliveryService companyDeliveryService;

    @GetMapping()
    public ApiResponse<?> getAllCompanyDelivery() {
        List<CompanyDeliveryResponseDto> response = companyDeliveryService.getAllCompanyDelivery();
        return ApiResponse.success(200, response);
    }

    @GetMapping("/{companyDeliveryId}")
    public ApiResponse<?> getCompanyDelivery(@PathVariable(name = "companyDeliveryId") Long hubDeliveryId) {
        CompanyDeliveryResponseDto response = companyDeliveryService.getCompanyDelivery(hubDeliveryId);
        return ApiResponse.success(200, response);
    }

    @DeleteMapping("/{companyDeliveryId}")
    public ApiResponse<?> deleteCompanyDelivery(@PathVariable(name = "companyDeliveryId") Long hubDeliveryId) {
        companyDeliveryService.deleteCompanyDelivery(hubDeliveryId);
        return ApiResponse.success(204, "배송 삭제 완료");
    }
}
