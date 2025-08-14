package com.ojo.mullyuojo.delivery.domain.hub_delivery;

import com.ojo.mullyuojo.delivery.domain.delivery.Delivery;
import com.ojo.mullyuojo.delivery.domain.hub_delivery.status.HubDeliveryStatus;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubDeliveryService {

    private final HubDeliveryRepository hubDeliveryRepository;
    public void createHubDeliveryByDelivery(Delivery delivery) {

        HubDelivery hubDelivery = new HubDelivery(
                delivery.getId(),
                HubDeliveryStatus.WAITING_AT_HUB,
                delivery.getOriginHubId(),
                delivery.getDestinationHubId(),
                10.0, //예상거리
                10.0, //예상시간
                1L //HubDeliveryManagerId
        );
    }

    public void changeStatus(Long id,HubDeliveryStatus status) {

        HubDelivery hubDelivery = findById(id);
        if (status.equals(HubDeliveryStatus.IN_TRANSIT_TO_HUB)){
            hubDelivery.changeStatus(HubDeliveryStatus.IN_TRANSIT_TO_HUB);
        }
        else if(status.equals(HubDeliveryStatus.ARRIVED_AT_DEST_HUB)){
            hubDelivery.changeStatus(HubDeliveryStatus.ARRIVED_AT_DEST_HUB);

        }

    }

    private HubDelivery findById(Long id) {
        return hubDeliveryRepository.findById(id).orElseThrow();
    }

}
