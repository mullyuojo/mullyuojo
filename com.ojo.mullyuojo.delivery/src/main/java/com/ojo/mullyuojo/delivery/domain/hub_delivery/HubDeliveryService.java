package com.ojo.mullyuojo.delivery.domain.hub_delivery;

import com.ojo.mullyuojo.delivery.domain.delivery.Delivery;
import com.ojo.mullyuojo.delivery.domain.delivery.client.company.DeliveryCompanyClient;
import com.ojo.mullyuojo.delivery.domain.delivery.client.company.DeliveryCompanyDto;
import com.ojo.mullyuojo.delivery.domain.delivery.client.hub.DeliveryHubClient;
import com.ojo.mullyuojo.delivery.domain.delivery.client.hub.DeliveryHubDto;
import com.ojo.mullyuojo.delivery.domain.delivery.client.user.DeliveryUserDto;
import com.ojo.mullyuojo.delivery.domain.hub_delivery.dto.HubDeliveryResponseDto;
import com.ojo.mullyuojo.delivery.domain.hub_delivery.status.HubDeliveryStatus;
import com.ojo.mullyuojo.delivery.domain.hub_move.HubMoveService;
import jakarta.ws.rs.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
//@PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','HUB_DELIVERY_MANAGER','COMPANY_MANAGER')")
public class HubDeliveryService {

    private final HubDeliveryRepository hubDeliveryRepository;
    private final DeliveryHubClient deliveryHubClient;
    private final DeliveryCompanyClient deliveryCompanyClient;
    private final HubMoveService hubMoveService;
    private final DeliveryUserDto user = new DeliveryUserDto(1L, "MASTER");

    @Transactional
    public void createHubDeliveryByDelivery(Delivery delivery) {

        Long originHubId = delivery.getOriginHubId();
        Long destinationHubId = delivery.getDestinationHubId();
        List<Double> disAndTime = hubMoveService.estimate(originHubId, destinationHubId);

        HubDelivery hubDelivery = new HubDelivery(
                delivery.getId(),
                HubDeliveryStatus.WAITING_AT_HUB,
                delivery.getOriginHubId(),
                delivery.getDestinationHubId(),
                disAndTime.get(0), //예상거리
                disAndTime.get(1), //예상시간
                delivery.getHubDeliveryManagerId() //HubDeliveryManagerId
        );
        hubDeliveryRepository.save(hubDelivery);
        log.info("Hub Delivery 생성 완료 : {}, {}, hub {} -> hub {}", hubDelivery.getDeliveryId(), hubDelivery.getStatus(), hubDelivery.getOriginHubId(), hubDelivery.getDestinationHubId());
    }

    @Transactional
    public List<HubDeliveryResponseDto> getAllHubDelivery() {
        String userRole = user.getUserRole();
        List<HubDelivery> hubDeliveryList;
        switch (userRole) {
            case "MASTER" -> hubDeliveryList = hubDeliveryRepository.findAll();
            case "HUB" -> {
                Long hubId = 1L;
                //허브한테 feingClient
                List<DeliveryHubDto> hubList = deliveryHubClient.findHubsByManager(user.getUserId());
                List<Long> hubIdList = hubList.stream().map(DeliveryHubDto::getId).toList();
                hubDeliveryList = hubDeliveryRepository.findAllByHubIds(hubIdList);
            }
            case "HUB_DELIVERY" -> {
                Long hubDeliveryManagerId = 1L;
                hubDeliveryList = hubDeliveryRepository.findAllByHubDeliveryManagerIdAndDeletedByIsNull(hubDeliveryManagerId);
            }
            case "COMPANY" -> {
                //업체한테 feignClient
                Long userId = user.getUserId();
                List<DeliveryCompanyDto> companyList = deliveryCompanyClient.findCompaniesByManager(userId);
                List<Long> companyIdList = companyList.stream().map(DeliveryCompanyDto::getId).toList();
                hubDeliveryList = hubDeliveryRepository.findAllByDestinationCompanyIds(companyIdList);
            }
            default -> throw new ForbiddenException("접근 권한이 없습니다.");
        }
        return hubDeliveryList.stream()
                .map(HubDeliveryResponseDto::from)
                .toList();
    }

    @Transactional
    public HubDeliveryResponseDto getHubDelivery(Long hubDeliveryId) {

        String userRole = user.getUserRole();
        HubDelivery hubDelivery = findById(hubDeliveryId);
        switch (userRole) {
            case "MASTER " -> {
                return HubDeliveryResponseDto.from(hubDelivery);
            }
            case "HUB" -> {
                Long hubId = 1L;
                //허브한테 feignClient
                List<DeliveryHubDto> hubList = deliveryHubClient.findHubsByManager(user.getUserId());
                List<Long> hubIdList = hubList.stream().map(DeliveryHubDto::getId).toList();
                if (!hubIdList.contains(hubDelivery.getOriginHubId()) || !hubIdList.contains(hubDelivery.getDestinationHubId())) {
                    throw new RuntimeException("접근 권한이 없습니다.");
                }
                return HubDeliveryResponseDto.from(hubDelivery);
            }
            case "HUB_DELIVERY" -> {
                Long hubDeliveryManagerId = 1L;
                if (!Objects.equals(hubDelivery.getHubDeliveryManagerId(), hubDeliveryManagerId)) {
                    throw new ForbiddenException("접근 권한이 없습니다.");
                }
                return HubDeliveryResponseDto.from(hubDelivery);
            }
            default -> throw new ForbiddenException("접근 권한이 없습니다.");
        }
    }

    @Transactional
    public void changeStatus(Long deliveryId, HubDeliveryStatus status) {

        HubDelivery hubDelivery = hubDeliveryRepository.findByDeliveryIdAndDeletedByIsNull(deliveryId);
        if (status.equals(HubDeliveryStatus.IN_TRANSIT_TO_HUB)) {
            hubDelivery.changeStatus(HubDeliveryStatus.IN_TRANSIT_TO_HUB);
            hubDelivery.setDepartureTime(LocalDateTime.now());
            log.info("Hub Delivery 상태 변경 : {}, {}", hubDelivery.getDeliveryId(), hubDelivery.getStatus());
        } else if (status.equals(HubDeliveryStatus.ARRIVED_AT_DEST_HUB)) {
            hubDelivery.changeStatus(HubDeliveryStatus.ARRIVED_AT_DEST_HUB);
            LocalDateTime arrivedTime = LocalDateTime.now();
            LocalDateTime departureTime = hubDelivery.getDepartureTime();

            Duration duration = Duration.between(arrivedTime, departureTime); // 초단위
            hubDelivery.setActualTime((double) duration.getSeconds());
            hubDelivery.setActualDistance(12345.0);
            log.info("Hub Delivery 상태 변경 : {}, {}, {}초", hubDelivery.getDeliveryId(), hubDelivery.getStatus(), hubDelivery.getActualTime());
        }
    }

    @Transactional
    public void updateHubDelivery(Delivery delivery) {
        HubDelivery hubDelivery = hubDeliveryRepository.findByDeliveryIdAndDeletedByIsNull(delivery.getId());
        hubDelivery.update(delivery);

    }
//    @PreAuthorize("hasRole('MASTER')")
    public void deleteHubDelivery(Long hubDeliveryId) {
        HubDelivery hubDelivery = findById(hubDeliveryId);
        hubDelivery.softDelete(1L);
    }

    private HubDelivery findById(Long id) {
        return hubDeliveryRepository.findByIdAndDeletedByIsNull(id).orElseThrow(() -> new RuntimeException("해당 배송을 찾을 수 없습니다."));
    }
}
