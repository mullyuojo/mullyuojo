package com.ojo.mullyuojo.delivery.domain.hub_move.dto;

import com.ojo.mullyuojo.delivery.domain.hub_move.HubMove;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record HubMoveResponseDto(

        Long originHubId,
        Long destinationHubId,
        Double time,
        Double distance,
        List<Long> managerList
) {
    public static HubMoveResponseDto from(HubMove hubMove) {
        return new HubMoveResponseDto(
                hubMove.getOriginHubId(),
                hubMove.getDestinationHubId(),
                hubMove.getTime(),
                hubMove.getDistance(),
                hubMove.getManagerList()
        );
    }
}
