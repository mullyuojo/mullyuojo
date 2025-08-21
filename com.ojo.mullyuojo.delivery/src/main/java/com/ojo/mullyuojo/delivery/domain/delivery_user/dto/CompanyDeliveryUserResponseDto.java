package com.ojo.mullyuojo.delivery.domain.delivery_user.dto;

import com.ojo.mullyuojo.delivery.domain.delivery_user.CompanyDeliveryUser;

public record CompanyDeliveryUserResponseDto(
        Long userId,
        Long hubId,
        Long sequence
) {
    public static CompanyDeliveryUserResponseDto from(CompanyDeliveryUser user) {
        return new CompanyDeliveryUserResponseDto(
                user.getUserId(),
                user.getHubId(),
                user.getSequence()
        );
    }
}
