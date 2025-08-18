package com.ojo.mullyuojo.delivery.domain.hub_delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HubDeliveryRepository extends JpaRepository<HubDelivery, Long> {


    Optional<HubDelivery> findByIdAndDeletedByIsNull(Long aLong);

    @Query("select d from hub_delivery_tb d where (d.originHubId in :hubIds or d.destinationHubId in :hubIds) and d.deletedBy = null")
    List<HubDelivery> findAllByHubIds(@Param("hubIds") List<Long> hubIdList);

    List<HubDelivery> findAllByHubDeliveryManagerIdAndDeletedByIsNull(Long hubDeliveryManagerId);

    @Query("select d from hub_delivery_tb d where d.destinationHubId in :companyIdList and d.deletedBy = null")
    List<HubDelivery> findAllByDestinationCompanyIds(@Param("companyIdList") List<Long> companyIdList);

    HubDelivery findByDeliveryIdAndDeletedByIsNull(Long deliveryId);
}
