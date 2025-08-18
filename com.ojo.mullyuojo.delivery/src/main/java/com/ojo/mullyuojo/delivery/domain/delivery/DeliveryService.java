package com.ojo.mullyuojo.delivery.domain.delivery;

import com.ojo.mullyuojo.delivery.domain.com_delivery.CompanyDeliveryService;
import com.ojo.mullyuojo.delivery.domain.com_delivery.status.CompanyDeliveryStatus;
import com.ojo.mullyuojo.delivery.domain.delivery.client.company.DeliveryCompanyClient;
import com.ojo.mullyuojo.delivery.domain.delivery.client.company.DeliveryCompanyDto;
import com.ojo.mullyuojo.delivery.domain.delivery.client.hub.DeliveryHubClient;
import com.ojo.mullyuojo.delivery.domain.delivery.client.hub.DeliveryHubDto;
import com.ojo.mullyuojo.delivery.domain.delivery.client.user.DeliveryUserDto;
import com.ojo.mullyuojo.delivery.domain.delivery.dto.DeliveryRequestDto;
import com.ojo.mullyuojo.delivery.domain.delivery.dto.DeliveryResponseDto;
import com.ojo.mullyuojo.delivery.domain.delivery.dto.DeliveryUpdateRequestDto;
import com.ojo.mullyuojo.delivery.domain.delivery.status.DeliveryStatus;
import com.ojo.mullyuojo.delivery.domain.hub_delivery.HubDeliveryService;
import com.ojo.mullyuojo.delivery.domain.hub_delivery.status.HubDeliveryStatus;
import jakarta.ws.rs.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final HubDeliveryService hubDeliveryService;
    private final CompanyDeliveryService companyDeliveryService;
    private final DeliveryHubClient deliveryHubClient;
    private final DeliveryCompanyClient deliveryCompanyClient;
    private final DeliveryUserDto user = new DeliveryUserDto(1L, "MASTER");

    @Transactional
    public List<DeliveryResponseDto> getAllDelivery() {

        String userRole = user.getUserRole();
        List<Delivery> deliveryList = new ArrayList<>();
        switch (userRole) {
            case "MASTER " -> deliveryList = deliveryRepository.findAll();
            case "HUB" -> {
                Long hubId = 1L;
                //허브한테 feingClient
                List<DeliveryHubDto> hubList = deliveryHubClient.findHubsByManager(user.getUserId());
                List<Long> hubIdList = hubList.stream().map(DeliveryHubDto::getId).toList();
                deliveryList = deliveryRepository.findAllByHubIds(hubIdList);
            }
            case "HUB_DELIVERY" -> {
                Long hubDeliveryManagerId = 1L;
                deliveryList = deliveryRepository.findAllByHubDeliveryManagerIdAndDeletedByIsNull(hubDeliveryManagerId);
            }
            case "COM_DELIVERY" -> {
                Long companyDeliveryManagerId = 1L;
                deliveryList = deliveryRepository.findAllByCompanyDeliveryManagerIdAndDeletedByIsNull(companyDeliveryManagerId);
            }
            case "COMPANY" -> {
                //업체한테 feignClient
                Long userId = user.getUserId();
                List<DeliveryCompanyDto> companyList = deliveryCompanyClient.findCompaniesByManager(userId);
                List<Long> companyIdList = companyList.stream().map(DeliveryCompanyDto::getId).toList();
                deliveryList = deliveryRepository.findAllByDestinationCompanyIds(companyIdList);
            }
        }
        return deliveryList.stream()
                .map(DeliveryResponseDto::from)
                .toList();
    }

    @Transactional
    public DeliveryResponseDto getDelivery(Long id) {

        String userRole = user.getUserRole();
        Delivery delivery = findById(id);
        switch (userRole) {
            case "MASTER " -> {
                return DeliveryResponseDto.from(delivery);
            }
            case "HUB" -> {
                Long hubId = 1L;
                //허브한테 feignClient
                List<DeliveryHubDto> hubList = deliveryHubClient.findHubsByManager(user.getUserId());
                List<Long> hubIdList = hubList.stream().map(DeliveryHubDto::getId).toList();
                if (!hubIdList.contains(delivery.getOriginHubId()) || !hubIdList.contains(delivery.getDestinationHubId())) {
                    throw new RuntimeException("접근 권한이 없습니다.");
                }
                return DeliveryResponseDto.from(delivery);
            }
            case "HUB_DELIVERY" -> {
                Long hubDeliveryManagerId = 1L;
                if (!Objects.equals(delivery.getHubDeliveryManagerId(), hubDeliveryManagerId)) {
                    throw new ForbiddenException("접근 권한이 없습니다.");
                }
                return DeliveryResponseDto.from(delivery);
            }
            case "COM_DELIVERY" -> {
                Long companyDeliveryManagerId = 1L;
                if (!Objects.equals(delivery.getCompanyDeliveryManagerId(), companyDeliveryManagerId)) {
                    throw new ForbiddenException("접근 권한이 없습니다.");
                }
                return DeliveryResponseDto.from(delivery);
            }
            case "COMPANY" -> {
                Long userId = user.getUserId();
                //업체한테 feignClient
                List<DeliveryCompanyDto> companyList = deliveryCompanyClient.findCompaniesByManager(userId);
                List<Long> companyIdList = companyList.stream().map(DeliveryCompanyDto::getId).toList();
                if (!companyIdList.contains(delivery.getDestinationCompanyId())) {
                    throw new ForbiddenException("접근 권한이 없습니다.");
                }
                return DeliveryResponseDto.from(delivery);
            }
        }


        return DeliveryResponseDto.from(delivery);
    }

    @Transactional
    public void createDelivery(DeliveryRequestDto requestDto) {

        if (!user.getUserRole().equals("MASTER")) {
            throw new ForbiddenException("생성 권한이 없습니다.");
        }
        Delivery delivery = new Delivery(requestDto.orderId(),
                requestDto.deliveryStatus(),
                requestDto.originHubId(),
                requestDto.destinationHubId(),
                requestDto.destinationCompanyId(),
                requestDto.companyManagerId(),
                requestDto.companyManagerSlackId(),
                requestDto.hubDeliveryManagerId(),
                requestDto.companyDeliveryManagerId());


        hubDeliveryService.createHubDeliveryByDelivery(delivery);
    }

    public void updateDelivery(Long id, DeliveryUpdateRequestDto requestDto) {

        if (!user.getUserRole().equals("MASTER") && !user.getUserRole().equals("HUB") && !user.getUserRole().equals("HUB_DELIVERY") && !user.getUserRole().equals("COM_DELIVERY")) {
            throw new ForbiddenException("수정 권한이 없습니다.");
        }
        Delivery delivery = findById(id);
        delivery.update(
                requestDto.orderId(),
                requestDto.deliveryStatus(),
                requestDto.originHubId(),
                requestDto.destinationHubId(),
                requestDto.destinationCompanyId(),
                requestDto.companyManagerId(),
                requestDto.companyManagerSlackId(),
                requestDto.hubDeliveryManagerId(),
                requestDto.companyDeliveryManagerId());

        //배송이 IN_TRANSIT_TO_HUB일 때 허브 배송 경로 기록 생성
        if (requestDto.deliveryStatus().equals(DeliveryStatus.IN_TRANSIT_TO_HUB)) {
            hubDeliveryService.changeStatus(delivery.getId(), HubDeliveryStatus.IN_TRANSIT_TO_HUB);
        } else if (requestDto.deliveryStatus().equals(DeliveryStatus.ARRIVED_AT_DEST_HUB)) {
            hubDeliveryService.changeStatus(delivery.getId(), HubDeliveryStatus.ARRIVED_AT_DEST_HUB);
        } else if (requestDto.deliveryStatus().equals(DeliveryStatus.OUT_FOR_DELIVERY)) {
            //배송이 OUT_FOR_DELIVERY일 때 허브 배송 경로 기록 변경 및 업체 배송 기록 생성
            companyDeliveryService.createCompanyDeliveryByDelivery(delivery);
        } else if (requestDto.deliveryStatus().equals(DeliveryStatus.IN_TRANSIT_TO_COMPANY)) {
            companyDeliveryService.changeStatus(delivery.getId(), CompanyDeliveryStatus.IN_TRANSIT_TO_COMPANY);
        } else if (requestDto.deliveryStatus().equals(DeliveryStatus.DELIVERED)) {
            companyDeliveryService.changeStatus(delivery.getId(), CompanyDeliveryStatus.DELIVERED);
        }
    }

    public void deleteDelivery(Long id) {
        if (!user.getUserRole().equals("MASTER") && !user.getUserRole().equals("HUB")) {
            throw new ForbiddenException("삭제 권한이 없습니다.");
        }
        Long userId = user.getUserId();
        Delivery delivery = findById(id);
        delivery.softDelete(userId);
    }

    private Delivery findById(Long id) {
        return deliveryRepository.findByIdAndDeletedByIsNull(id).orElseThrow(() -> new RuntimeException("해당 배송을 찾을 수 없습니다."));
    }


}
