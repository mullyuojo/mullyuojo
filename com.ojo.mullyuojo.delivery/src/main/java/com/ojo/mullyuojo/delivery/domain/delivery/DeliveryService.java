package com.ojo.mullyuojo.delivery.domain.delivery;

import com.ojo.mullyuojo.delivery.domain.com_delivery.CompanyDeliveryService;
import com.ojo.mullyuojo.delivery.domain.com_delivery.status.CompanyDeliveryStatus;
import com.ojo.mullyuojo.delivery.domain.delivery.client.hub.DeliveryHubClient;
import com.ojo.mullyuojo.delivery.domain.delivery.client.hub.DeliveryHubDto;
import com.ojo.mullyuojo.delivery.domain.delivery.dto.DeliveryRequestDto;
import com.ojo.mullyuojo.delivery.domain.delivery.dto.DeliveryResponseDto;
import com.ojo.mullyuojo.delivery.domain.delivery.dto.DeliveryUpdateRequestDto;
import com.ojo.mullyuojo.delivery.domain.delivery.status.DeliveryStatus;
import com.ojo.mullyuojo.delivery.domain.delivery_user.CompanyDeliveryUserService;
import com.ojo.mullyuojo.delivery.domain.hub_delivery.HubDeliveryService;
import com.ojo.mullyuojo.delivery.domain.hub_delivery.status.HubDeliveryStatus;
import com.ojo.mullyuojo.delivery.utils.QueueMessage;
import jakarta.ws.rs.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
//@PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','HUB_DELIVERY_MANAGER','COMPANY_DELIVERY_MANAGER','COMPANY_MANAGER')")
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final HubDeliveryService hubDeliveryService;
    private final CompanyDeliveryService companyDeliveryService;
    private final DeliveryHubClient deliveryHubClient;
    private final CompanyDeliveryUserService companyDeliveryUserService;


    @Transactional
    public List<DeliveryResponseDto> getAllDelivery(Authentication auth) {
        Long userId = Long.valueOf((String)auth.getPrincipal());
        String userRole = auth.getAuthorities().toString();
        log.info("user Id : {}, user Role : {}", userId, userRole);

        List<Delivery> deliveryList = new ArrayList<>();
        switch (userRole) {
            case "[MASTER]" -> deliveryList = deliveryRepository.findAll();
            case "[HUB_MANAGER]" -> {
                //허브한테 feignClient
                List<DeliveryHubDto> hubList = deliveryHubClient.findHubsByManager(userId);
                List<Long> hubIdList = hubList.stream().map(DeliveryHubDto::getId).toList();
                deliveryList = deliveryRepository.findAllByHubIds(hubIdList);
            }
            case "[HUB_DELIVERY_MANAGER]" -> {
                deliveryList = deliveryRepository.findAllByHubDeliveryManagerIdAndDeletedByIsNull(userId);
            }
            case "[COMPANY_DELIVERY_MANAGER]" -> {
                deliveryList = deliveryRepository.findAllByCompanyDeliveryManagerIdAndDeletedByIsNull(userId);
            }
            case "[COMPANY]" -> {
                //업체한테 feignClient
                deliveryList = deliveryRepository.findAllByCompanyManagerIdAndDeletedByIsNull(userId);
            }
        }
        return deliveryList.stream()
                .map(DeliveryResponseDto::from)
                .toList();
    }

    //@PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','HUB_DLIVERY_MANAGER','COM_DLIVERY_MANAGER')")
    @Transactional
    public DeliveryResponseDto getDelivery(Long id, Authentication auth) {
        Long userId = Long.valueOf((String)auth.getPrincipal());
        String userRole = auth.getAuthorities().toString();
        log.info("user Id : {}, user Role : {}", userId, userRole);

        Delivery delivery = findById(id);
        switch (userRole) {
            case "[MASTER]" -> {
                return DeliveryResponseDto.from(delivery);
            }
            case "[HUB_MANAGER]" -> {
                //허브한테 feignClient
                List<DeliveryHubDto> hubList = deliveryHubClient.findHubsByManager(userId);
                List<Long> hubIdList = hubList.stream().map(DeliveryHubDto::getId).toList();
                if (!hubIdList.contains(delivery.getOriginHubId()) || !hubIdList.contains(delivery.getDestinationHubId())) {
                    throw new RuntimeException("접근 권한이 없습니다.");
                }
                return DeliveryResponseDto.from(delivery);
            }
            case "[HUB_DELIVERY_MANAGER]" -> {
                if (!Objects.equals(delivery.getHubDeliveryManagerId(), userId)) {
                    throw new ForbiddenException("접근 권한이 없습니다.");
                }
                return DeliveryResponseDto.from(delivery);
            }
            case "[COMPANY_DELIVERY_MANAGER]" -> {
                if (!Objects.equals(delivery.getCompanyDeliveryManagerId(), userId)) {
                    throw new ForbiddenException("접근 권한이 없습니다.");
                }
                return DeliveryResponseDto.from(delivery);
            }
            case "[COMPANY_MANAGER]" -> {
                //업체한테 feignClient
                if (!Objects.equals(delivery.getCompanyManagerId(), userId)) {
                    throw new ForbiddenException("수정 권한이 없습니다.");
                }
                return DeliveryResponseDto.from(delivery);
            }
        }


        return DeliveryResponseDto.from(delivery);
    }

    //@PreAuthorize("hasRole('MASTER')")
    @Transactional
    public void createDelivery(DeliveryRequestDto requestDto, Authentication auth, QueueMessage queueMessage) {
        Long orderId = queueMessage.getOrderId();
        Long deliveryUserId = companyDeliveryUserService.findNextUserInHub(requestDto.destinationHubId()); // 배송 시퀀스에 따라 배정되는 배송 담당자

        Delivery delivery = new Delivery(
                orderId,
                requestDto.deliveryStatus(),
                requestDto.originHubId(),
                requestDto.destinationHubId(),
                requestDto.destinationCompanyId(),
                requestDto.companyManagerId(),
                requestDto.companyManagerSlackId(),
                requestDto.hubDeliveryManagerId(),
                deliveryUserId
        );
        deliveryRepository.save(delivery);
        hubDeliveryService.createHubDeliveryByDelivery(delivery);
    }

    //@PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','HUB_DLIVERY_MANAGER','COM_DLIVERY_MANAGER','COMPANY_MANAGER')")
    @Transactional
    public void updateDelivery(Long id, DeliveryUpdateRequestDto requestDto, Authentication auth) {
        Long userId = Long.valueOf((String)auth.getPrincipal());
        String userRole = auth.getAuthorities().toString();
        log.info("user Id : {}, user Role : {}", userId, userRole);

        Delivery delivery = findById(id);

        switch (userRole) {
            case "[HUB_MANAGER]" -> {
                //허브한테 feignClient
                List<DeliveryHubDto> hubList = deliveryHubClient.findHubsByManager(userId);
                List<Long> hubIdList = hubList.stream().map(DeliveryHubDto::getId).toList();
                if (!hubIdList.contains(delivery.getOriginHubId()) || !hubIdList.contains(delivery.getDestinationHubId())) {
                    throw new RuntimeException("수정 권한이 없습니다.");
                }
            }
            case "[HUB_DLIVERY_MANAGER]" -> {
                if (!Objects.equals(delivery.getHubDeliveryManagerId(), userId)) {
                    throw new ForbiddenException("수정 권한이 없습니다.");
                }
            }
            case "[COM_DLIVERY_MANAGER]" -> {
                if (!Objects.equals(delivery.getCompanyDeliveryManagerId(), userId)) {
                    throw new ForbiddenException("수정 권한이 없습니다.");
                }
            }
            case "[COMPANY_MANAGER]" -> {
                if (!Objects.equals(delivery.getCompanyManagerId(), userId)) {
                    throw new ForbiddenException("수정 권한이 없습니다.");
                }
            }
        }
        delivery.update(
                requestDto.orderId(),
                requestDto.deliveryStatus(),
                requestDto.originHubId(),
                requestDto.destinationHubId(),
                requestDto.destinationCompanyId(),
                requestDto.companyManagerId(),
                requestDto.companyManagerSlackId(),
                requestDto.hubDeliveryManagerId());

        //배송이 IN_TRANSIT_TO_HUB일 때 허브 배송 경로 기록 생성
        if (requestDto.deliveryStatus().equals(DeliveryStatus.IN_TRANSIT_TO_HUB)) {
            hubDeliveryService.changeStatus(delivery.getId(), HubDeliveryStatus.IN_TRANSIT_TO_HUB);
            hubDeliveryService.updateHubDelivery(delivery);
        } else if (requestDto.deliveryStatus().equals(DeliveryStatus.ARRIVED_AT_DEST_HUB)) {
            hubDeliveryService.changeStatus(delivery.getId(), HubDeliveryStatus.ARRIVED_AT_DEST_HUB);
            hubDeliveryService.updateHubDelivery(delivery);
        } else if (requestDto.deliveryStatus().equals(DeliveryStatus.OUT_FOR_DELIVERY)) {
            //배송이 OUT_FOR_DELIVERY일 때 허브 배송 경로 기록 변경 및 업체 배송 기록 생성
            companyDeliveryService.createCompanyDeliveryByDelivery(delivery);
            companyDeliveryService.updateCompanyDelivery(delivery);
        } else if (requestDto.deliveryStatus().equals(DeliveryStatus.IN_TRANSIT_TO_COMPANY)) {
            companyDeliveryService.changeStatus(delivery.getId(), CompanyDeliveryStatus.IN_TRANSIT_TO_COMPANY);
            companyDeliveryService.updateCompanyDelivery(delivery);
        } else if (requestDto.deliveryStatus().equals(DeliveryStatus.DELIVERED)) {
            companyDeliveryService.changeStatus(delivery.getId(), CompanyDeliveryStatus.DELIVERED);
            companyDeliveryService.updateCompanyDelivery(delivery);
        }
    }

    //@PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    public void deleteDelivery(Long id, Authentication auth) {
        Long userId = Long.valueOf((String)auth.getPrincipal());
        Delivery delivery = findById(id);
        delivery.softDelete(userId);
    }

    private Delivery findById(Long id) {
        return deliveryRepository.findByIdAndDeletedByIsNull(id).orElseThrow(() -> new RuntimeException("해당 배송을 찾을 수 없습니다."));
    }


}
