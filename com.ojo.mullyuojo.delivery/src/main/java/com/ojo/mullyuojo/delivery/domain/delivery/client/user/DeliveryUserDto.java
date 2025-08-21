package com.ojo.mullyuojo.delivery.domain.delivery.client.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class DeliveryUserDto {
    Long userId;
    String userRole;

    public DeliveryUserDto(Long id, String userRole){
        this.userId = id;
        this.userRole = userRole;
    }
}
