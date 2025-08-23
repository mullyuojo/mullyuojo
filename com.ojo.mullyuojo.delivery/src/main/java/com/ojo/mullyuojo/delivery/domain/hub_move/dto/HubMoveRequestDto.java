package com.ojo.mullyuojo.delivery.domain.hub_move.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

public record HubMoveRequestDto(

        @NotNull
        Long originHubId,
        @NotNull
        Long destinationHubId,
        @NotNull
        Double time,
        @NotNull
        Double distance

) {
}
