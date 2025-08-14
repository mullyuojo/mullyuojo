package com.ojo.mullyuojo.delivery.domain.hub_delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HubDeliveryRepository extends JpaRepository<HubDelivery,Long> {
}
