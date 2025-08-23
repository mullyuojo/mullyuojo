package com.ojo.mullyuojo.slackmessage.domain.slackmessage.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("DELIVERY-SERVICE")
public interface CompanyDeliveryClient {

    @GetMapping("/com-delivery-channels/{companyDeliveryId}")
    CompanyDeliveryResponseDto getCompanyDelivery(@PathVariable(name = "companyDeliveryId") Long hubDeliveryId);
}
