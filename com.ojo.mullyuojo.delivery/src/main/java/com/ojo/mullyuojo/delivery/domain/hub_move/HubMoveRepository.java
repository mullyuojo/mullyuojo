package com.ojo.mullyuojo.delivery.domain.hub_move;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HubMoveRepository extends JpaRepository<HubMove, Long> {

    HubMove findByOriginHubIdAndDestinationHubIdAndDeletedByIsNull(Long originHubId, Long destinationHubId);

    Optional<HubMove> findByIdAndDeletedByIsNull(Long id);

    @Query("select h from hub_move_tb h where h.deletedBy is null")
    List<HubMove> findAllAndDeletedByIsNull();
}
