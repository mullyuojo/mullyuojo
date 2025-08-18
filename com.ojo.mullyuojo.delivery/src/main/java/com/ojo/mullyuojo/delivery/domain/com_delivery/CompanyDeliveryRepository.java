package com.ojo.mullyuojo.delivery.domain.com_delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyDeliveryRepository extends JpaRepository<CompanyDelivery, Long> {
    CompanyDelivery findByDeliveryIdAndDeletedByIsNull(Long deliveryId);

    Optional<CompanyDelivery> findByIdAndDeletedByIsNull(Long id);

    @Query("select d from com_delivery_tb d where d.originHubId in :hubIds and d.deletedBy = null")
    List<CompanyDelivery> findAllByHubIds(@Param("hubIds") List<Long> hubIdList);

    List<CompanyDelivery> findAllByCompanyDeliveryManagerIdAndDeletedByIsNull(Long companyDeliveryManagerId);

    @Query("select d from com_delivery_tb d where d.destinationCompanyId in :companyIdList and d.deletedBy = null")
    List<CompanyDelivery> findAllByDestinationCompanyIds(@Param("companyIdList") List<Long> companyIdList);
}
