package com.ojo.mullyuojo.delivery.domain.delivery;

import com.ojo.mullyuojo.delivery.domain.com_delivery.CompanyDeliveryService;
import com.ojo.mullyuojo.delivery.domain.delivery.client.DeliveryUserDto;
import com.ojo.mullyuojo.delivery.domain.delivery.dto.DeliveryRequestDto;
import com.ojo.mullyuojo.delivery.domain.delivery.dto.DeliveryResponseDto;
import com.ojo.mullyuojo.delivery.domain.delivery.status.DeliveryStatus;
import com.ojo.mullyuojo.delivery.domain.hub_delivery.HubDeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final HubDeliveryService hubDeliveryService;
    private final CompanyDeliveryService companyDeliveryService;
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

    public void createDelivery(DeliveryRequestDto requestDto) {
        Delivery delivery = new Delivery(requestDto.orderId(),
                requestDto.deliveryStatus(),
                requestDto.originHubId(),
                requestDto.destinationHubId(),
                requestDto.companyAddress(),
                requestDto.companyManagerId(),
                requestDto.companyManagerSlackId(),
                requestDto.companyDeliveryManagerId());

        hubDeliveryService.createHubDeliveryByDelivery(delivery);
        companyDeliveryService.createCompanyDeliveryByDelivery(delivery);
    }

    public void changeStatus(Long id, DeliveryStatus status) {

        Delivery delivery = findById(id);
        if (status.equals(DeliveryStatus.IN_TRANSIT_TO_HUB)){
            delivery.changeStatus(DeliveryStatus.IN_TRANSIT_TO_HUB);
        } else if(status.equals(DeliveryStatus.ARRIVED_AT_DEST_HUB)){
            delivery.changeStatus(DeliveryStatus.ARRIVED_AT_DEST_HUB);
        } else if(status.equals(DeliveryStatus.OUT_FOR_DELIVERY)){
            delivery.changeStatus(DeliveryStatus.OUT_FOR_DELIVERY);
        } else if(status.equals(DeliveryStatus.IN_TRANSIT_TO_COMPANY)){
            delivery.changeStatus(DeliveryStatus.IN_TRANSIT_TO_COMPANY);
        } else if(status.equals(DeliveryStatus.DELIVERED)){
            delivery.changeStatus(DeliveryStatus.DELIVERED);
        }
    }

    public void deleteDelivery(Long id) {
        Long userId = user.getUserId();
        Delivery delivery = findById(id);
        delivery.softDelete(userId);
    }

    private Delivery findById(Long id) {
        return deliveryRepository.findByIdAndDeletedByIsNull(id).orElseThrow(() -> new RuntimeException("해당 배송을 찾을 수 없습니다."));
    }


}
