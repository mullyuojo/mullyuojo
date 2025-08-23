package com.ojo.mullyuojo.delivery.domain.delivery.client.hub;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("hub-client")
public interface DeliveryHubClient {

    @GetMapping("/hubs/{managerId}")
    List<DeliveryHubDto> findHubsByManager(@PathVariable(name = "managerId") Long managerId);
}
