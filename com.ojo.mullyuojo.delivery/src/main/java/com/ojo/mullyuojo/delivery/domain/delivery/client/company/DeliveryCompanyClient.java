package com.ojo.mullyuojo.delivery.domain.delivery.client.company;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("company-client")
public interface DeliveryCompanyClient {

    @GetMapping("/companies/{managerId}")
    List<DeliveryCompanyDto> findCompaniesByManager(@PathVariable(name = "managerId") Long managerId);
}
