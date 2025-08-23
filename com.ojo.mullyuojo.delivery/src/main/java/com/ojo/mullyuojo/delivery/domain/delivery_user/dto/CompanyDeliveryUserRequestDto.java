package com.ojo.mullyuojo.delivery.domain.delivery_user.dto;

import jakarta.validation.constraints.NotNull;

public record CompanyDeliveryUserRequestDto(
        @NotNull
        Long userId,
        @NotNull
        Long hubId
) {
}
