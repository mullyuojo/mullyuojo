package com.ojo.mullyuojo.delivery.domain.com_delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDeliveryRepository extends JpaRepository<CompanyDelivery,Long> {
}
