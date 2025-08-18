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

    public DeliveryResponseDto getDelivery(Long id) {


        Delivery delivery = findById(id);
//        //허브관리자
//        String role = "HUB";
//        if ( role.equals("HUB")) {
//            //배송의 출발 허브 ID & 도착 허브 ID를 통해 허브 객체 가져오기 ( HUB FeignClient )
//            //해당 허브의 허브 담당자 id 가져오기
//            //본인 허브인지 확인
//        }
//        //허브배송관리자 ---> 소속된 허브가 없는디?...
//        String role2 = "HUB_DELIVERY";
//        if ( role2.equals("DELIVERY")) {
//            //배송담당자_허브배송담당자 테이블에서 허브ID 가져오기 ( DeliveryUser FeignClient)
//            //배송의 출발허브 ID & 도착 허브 ID와 비교
//            //본인 허브인지 확인
//        }
        //업체 배송 관리자
//        String role3 = "COM_DELIVERY";
//        if ( role2.equals("COM_DELIVERY")) {
//            //배송의 업체 배송 담당자와 비교
//            //본인 담당인지 확인
//        }
//

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
