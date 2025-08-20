package com.ojo.mullyuojo.delivery.domain.delivery;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("select d from delivery_tb d where d.originHubId = :hubId or d.destinationHubId = :hubId and d.deletedBy = null order by d.id desc")
    List<Delivery> findAllByHubId(@Param("hubId") Long hubId);

    @Query("select d from delivery_tb d where (d.originHubId in :hubIds or d.destinationHubId in :hubIds) and d.deletedBy = null")
    List<Delivery> findAllByHubIds(@Param("hubIds") List<Long> hubIds);

    List<Delivery> findAllByHubDeliveryManagerIdAndDeletedByIsNull(Long id);

    List<Delivery> findAllByCompanyDeliveryManagerIdAndDeletedByIsNull(Long id);

    @Query("select d from delivery_tb d where d.destinationCompanyId in :companyIds ")
    List<Delivery> findAllByDestinationCompanyIds(@Param("companyIds") List<Long> companyIds);

    Optional<Delivery> findByIdAndDeletedByIsNull(Long aLong);
}
