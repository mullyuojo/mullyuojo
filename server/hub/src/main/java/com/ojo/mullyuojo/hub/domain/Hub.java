package com.ojo.mullyuojo.hub.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ojo.mullyuojo.hub.application.dtos.HubRequestDto;
import com.ojo.mullyuojo.hub.application.dtos.HubResponseDto;
import com.ojo.mullyuojo.hub.application.dtos.companyListDto.CompanyDto;
import com.ojo.mullyuojo.hub.application.dtos.deliveryManagerListDto.DeliveryDto;
import com.ojo.mullyuojo.hub.application.exception.BusinessException;
import com.ojo.mullyuojo.hub.application.exception.ErrorCode;
import com.ojo.mullyuojo.hub.application.security.AccessContext;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "hub", indexes = { @Index(name = "idx_hub_name", columnList = "hub_name")})
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE hub SET deleted_at = now(), deleted_by = ? WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Hub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hub_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String hubName;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(length = 50)
    private String province;

    @CreatedBy
    @Column(name = "created_by", nullable = false)
    private String writer;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Version
    private Long version;

    // 업체 리스트 테이블
    @OneToMany(mappedBy = "hub", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @BatchSize(size = 10)
    private List<CompanyList> companyLists = new ArrayList<>();

    // 업체 배송 담당자 테이블
    @OneToMany(mappedBy = "hub", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private List<DeliveryManagerList> deliveryManagerLists = new ArrayList<>();




    public static Hub createHub(HubRequestDto dto, String userId, AccessContext ctx,
                                BigDecimal latitude, BigDecimal longitude) {
        requiredNonBlank(dto.getHubName(), "name");
        requiredNonBlank(dto.getAddress(), "address");
        requiredNonBlank(dto.getProvince(), "province");

        //Builder 객체 생성
        Hub.HubBuilder builder = Hub.builder()
                .hubName(dto.getHubName())
                .address(dto.getAddress())
                .province(dto.getProvince())
                .latitude(latitude)
                .longitude(longitude)
                .writer(userId);

        // Hub 엔티티 생성
        Hub hub = builder.build();

        //Dto -> 엔티티 변환 후 업체 리스트 세팅
        if(dto.getCompanyLists() != null && !dto.getCompanyLists().isEmpty()) {
            List<CompanyList> companyEntities = new ArrayList<>();
            for(CompanyDto companyDto : dto.getCompanyLists()) {
                CompanyList company = new CompanyList();
                company.setName(companyDto.getName().trim());
                company.setHub(hub); //연관관계 설정
                companyEntities.add(company);
            }
            hub.setCompanyLists(companyEntities);
        }

        //Dto -> 엔티티 변환 후 배송 담당자 리스트 세팅
        if(dto.getDeliveryManagerLists() != null && !dto.getDeliveryManagerLists().isEmpty()) {
            List<DeliveryManagerList> managerEntities = new ArrayList<>();
            for(DeliveryDto deliveryDto : dto.getDeliveryManagerLists()) {
                DeliveryManagerList manager = new DeliveryManagerList();
                manager.setName(deliveryDto.getName().trim());
                manager.setHub(hub); //연관관계 설정
                managerEntities.add(manager);
            }
            hub.setDeliveryManagerLists(managerEntities);
        }
        return hub;
    }

    public void updateHub(HubRequestDto dto, BigDecimal latitude, BigDecimal longitude) {
        // 허브 이름 업데이트
        if(dto.getHubName() != null && !dto.getHubName().isBlank()){
            String trimmedName = dto.getHubName().trim();
            if(trimmedName.isEmpty()){
                throw new BusinessException(ErrorCode.INVALID_INPUT, "허브명을 공백으로 입력할 수 없습니다");
            }
            if(!trimmedName.equals(this.hubName)){
                this.hubName = trimmedName;
            }
        }
        // 주소 및 좌표 업데이트
        if(dto.getAddress() != null && !dto.getAddress().isBlank()){
            String trimmedAddress = dto.getAddress().trim();
            if(trimmedAddress.isEmpty()){
                throw new BusinessException(ErrorCode.INVALID_INPUT, "허브 주소를 공백으로 입력할 수 없습니다");
            }
            if(!trimmedAddress.equals(this.address)){
                this.address = trimmedAddress;
                this.latitude = latitude;
                this.longitude = longitude;
            }
        }
        // 지역 업데이트
        if(dto.getProvince() != null && !dto.getProvince().isBlank()){
            String trimmedProvince = dto.getProvince().trim();
            if(trimmedProvince.isEmpty()){
                throw new BusinessException(ErrorCode.INVALID_INPUT, "허브 지역을 공백으로 입력할 수 없습니다");
            }
            if(!trimmedProvince.equals(this.province)){
                this.province = trimmedProvince;
            }
        }

        // 업체 리스트 업데이트
        if(dto.getCompanyLists() != null){
            this.companyLists.clear();
            dto.getCompanyLists().forEach(dtoCompany -> {
                CompanyList company = new CompanyList();
                company.setName(dtoCompany.getName().trim());
                company.setHub(this); // 연관관계 재설정
                this.companyLists.add(company);
            });
        }

        // 배송 담당자 리스트 업데이트
        if(dto.getDeliveryManagerLists() != null){
            this.deliveryManagerLists.clear();
            dto.getDeliveryManagerLists().forEach(dtoManager -> {
                DeliveryManagerList manager = new DeliveryManagerList();
                manager.setName(dtoManager.getName().trim());
                manager.setHub(this); // 연관관계 재설정
                this.deliveryManagerLists.add(manager);
            });
        }


        this.updatedAt = LocalDateTime.now();
    }

    public void deleteHub(String deletedBy) {
        if(this.deletedAt != null) {
            throw new BusinessException(ErrorCode.HUB_NOT_FOUND, "이미 삭제된 허브입니다.");
        }
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

    public HubResponseDto toResponseDto(){
        return HubResponseDto.builder()
                .id(this.id)
                .hubName(this.hubName)
                .address(this.address)
                .province(this.province)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .companyLists(this.getCompanyLists() != null
                        ? this.getCompanyLists().stream()
                        .map(c-> new CompanyDto(c.getId(), c.getName()))
                        .toList()
                        :new ArrayList<>()
                )
                .deliveryManagerLists(this.getDeliveryManagerLists() != null
                        ? this.getDeliveryManagerLists().stream()
                        .map(d -> new DeliveryDto(d.getId(), d.getName()))
                        .toList()
                        :new ArrayList<>()
                )
                // 업체 리스트 DTO 변환
                // 배송 담당자 리스트 DTO 변환
                .build();
    }

    // 유효성 검사
    private static void requiredNonBlank(String s, String f){
        if(s == null || s.isBlank()){
            throw new BusinessException(ErrorCode.INVALID_INPUT, f+" 칸을 입력해주세요.");
        }
    }
}
