package com.ojo.mullyuojo.delivery.domain.com_delivery;

import com.ojo.mullyuojo.delivery.domain.com_delivery.dto.CompanyDeliveryResponseDto;
import com.ojo.mullyuojo.delivery.domain.com_delivery.status.CompanyDeliveryStatus;
import com.ojo.mullyuojo.delivery.domain.delivery.Delivery;
import com.ojo.mullyuojo.delivery.domain.delivery.client.company.DeliveryCompanyClient;
import com.ojo.mullyuojo.delivery.domain.delivery.client.company.DeliveryCompanyDto;
import com.ojo.mullyuojo.delivery.domain.delivery.client.hub.DeliveryHubClient;
import com.ojo.mullyuojo.delivery.domain.delivery.client.hub.DeliveryHubDto;
import com.ojo.mullyuojo.delivery.domain.delivery.client.user.DeliveryUserDto;
import jakarta.ws.rs.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyDeliveryService {
    private final CompanyDeliveryRepository companyDeliveryRepository;
    private final DeliveryHubClient deliveryHubClient;
    private final DeliveryCompanyClient deliveryCompanyClient;
    private final DeliveryUserDto user = new DeliveryUserDto(1L, "MASTER");

    @Transactional
    public void createCompanyDeliveryByDelivery(Delivery delivery) {
        CompanyDelivery companyDelivery = new CompanyDelivery(
                delivery.getId(),
                CompanyDeliveryStatus.OUT_FOR_DELIVERY,
                delivery.getOriginHubId(),
                delivery.getDestinationCompanyId(),
                10.0, //예상 거리
                60.0, // 예상 시간
                delivery.getCompanyDeliveryManagerId()
        );
        companyDeliveryRepository.save(companyDelivery);
        log.info("Company Delivery 생성 완료 : {}, {}, hub {} -> com {}", companyDelivery.getDeliveryId(), companyDelivery.getStatus(), companyDelivery.getOriginHubId(), companyDelivery.getDestinationCompanyId());
    }

    @Transactional
    public List<CompanyDeliveryResponseDto> getAllCompanyDelivery() {

        String userRole = user.getUserRole();
        List<CompanyDelivery> companyDeliveryList = new ArrayList<>();
        switch (userRole) {
            case "MASTER " -> companyDeliveryList = companyDeliveryRepository.findAll();
            case "HUB" -> {
                Long hubId = 1L;
                //허브한테 feingClient
                List<DeliveryHubDto> hubList = deliveryHubClient.findHubsByManager(user.getUserId());
                List<Long> hubIdList = hubList.stream().map(DeliveryHubDto::getId).toList();
                companyDeliveryList = companyDeliveryRepository.findAllByHubIds(hubIdList);
            }
            case "COM_DELIVERY" -> {
                Long companyDeliveryManagerId = 1L;
                companyDeliveryList = companyDeliveryRepository.findAllByCompanyDeliveryManagerIdAndDeletedByIsNull(companyDeliveryManagerId);
            }
            case "COMPANY" -> {
                //업체한테 feignClient
                Long userId = user.getUserId();
                List<DeliveryCompanyDto> companyList = deliveryCompanyClient.findCompaniesByManager(userId);
                List<Long> companyIdList = companyList.stream().map(DeliveryCompanyDto::getId).toList();
                companyDeliveryList = companyDeliveryRepository.findAllByDestinationCompanyIds(companyIdList);
            }
        }
        return companyDeliveryList.stream()
                .map(CompanyDeliveryResponseDto::from)
                .toList();
    }

    public CompanyDeliveryResponseDto getCompanyDelivery(Long companyDeliveryId) {

        String userRole = user.getUserRole();
        CompanyDelivery companyDelivery = findById(companyDeliveryId);
        switch (userRole) {
            case "MASTER " -> {
                return CompanyDeliveryResponseDto.from(companyDelivery);
            }
            case "COM_DELIVERY" -> {
                Long companyDeliveryManagerId = 1L;
                if (!Objects.equals(companyDelivery.getCompanyDeliveryManagerId(), companyDeliveryManagerId)) {
                    throw new ForbiddenException("담당 업체 배송 기록이 아닙니다.");
                }
                return CompanyDeliveryResponseDto.from(companyDelivery);
            }
            case "COMPANY" -> {
                Long userId = user.getUserId();
                //업체한테 feignClient
                List<DeliveryCompanyDto> companyList = deliveryCompanyClient.findCompaniesByManager(userId);
                List<Long> companyIdList = companyList.stream().map(DeliveryCompanyDto::getId).toList();
                if (!companyIdList.contains(companyDelivery.getDestinationCompanyId())) {
                    throw new ForbiddenException("소속된 업체 배송 기록이 아닙니다.");
                }
                return CompanyDeliveryResponseDto.from(companyDelivery);
            }
            default -> throw new ForbiddenException("접근 권한이 없습니다.");
        }
    }

    @Transactional
    public void changeStatus(Long deliveryId, CompanyDeliveryStatus status) {

        CompanyDelivery companyDelivery = companyDeliveryRepository.findByDeliveryIdAndDeletedByIsNull(deliveryId);

        if (status.equals(CompanyDeliveryStatus.IN_TRANSIT_TO_COMPANY)) {
            companyDelivery.changeStatus(CompanyDeliveryStatus.IN_TRANSIT_TO_COMPANY);
            companyDelivery.setDepartureTime(LocalDateTime.now());
            log.info("Company Delivery 상태 변경 : {}, {}", companyDelivery.getDeliveryId(), companyDelivery.getStatus());

        } else if (status.equals(CompanyDeliveryStatus.DELIVERED)) {
            companyDelivery.changeStatus(CompanyDeliveryStatus.DELIVERED);
            LocalDateTime arrivedTime = LocalDateTime.now();
            LocalDateTime departureTime = companyDelivery.getDepartureTime();

            Duration duration = Duration.between(arrivedTime, departureTime); // 초단위
            companyDelivery.setActualTime((double) duration.getSeconds());
            companyDelivery.setActualDistance(12345.0);
            log.info("Company Delivery 상태 변경 : {}, {}, {}초", companyDelivery.getDeliveryId(), companyDelivery.getStatus(), companyDelivery.getActualTime());
        }
    }

    @Transactional
    public void updateCompanyDelivery(Delivery delivery) {
        CompanyDelivery companyDelivery = companyDeliveryRepository.findByDeliveryIdAndDeletedByIsNull(delivery.getId());
        companyDelivery.update(delivery);
    }

    public void deleteCompanyDelivery(Long hubDeliveryId) {
        CompanyDelivery companyDelivery = findById(hubDeliveryId);
        companyDelivery.softDelete(1L);
    }

    private CompanyDelivery findById(Long id) {
        return companyDeliveryRepository.findByIdAndDeletedByIsNull(id).orElseThrow(() -> new RuntimeException("해당 배송을 찾을 수 없습니다."));
    }
}
