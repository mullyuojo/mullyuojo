package com.ojo.mullyuojo.hub.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ojo.mullyuojo.hub.application.dtos.GeocodeResponse;
import com.ojo.mullyuojo.hub.application.dtos.HubRequestDto;
import com.ojo.mullyuojo.hub.application.dtos.HubResponseDto;
import com.ojo.mullyuojo.hub.application.exception.BusinessException;
import com.ojo.mullyuojo.hub.application.exception.ErrorCode;
import com.ojo.mullyuojo.hub.application.security.AccessContext;
import com.ojo.mullyuojo.hub.application.service.GeoService;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "hubs", indexes = { @Index(name = "idx_hubs_name", columnList = "hubs_name")})
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE hubs SET deleted_at = now(), deleted_by = ? WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Hub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public HubResponseDto toResponseDto(){
        return HubResponseDto.builder()
                .id(this.id)
                .hubName(this.hubName)
                .address(this.address)
                .province(this.province)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .build();
    }

    public static Hub createHub(HubRequestDto dto, String userId, AccessContext ctx,
                                BigDecimal latitude, BigDecimal longitude) {
        requiredNonBlank(dto.getHubName(), "name");
        requiredNonBlank(dto.getAddress(), "address");
        requiredNonBlank(dto.getProvince(), "province");

        return Hub.builder()
                .hubName(dto.getHubName())
                .address(dto.getAddress())
                .province(dto.getProvince())
                .latitude(latitude)
                .longitude(longitude)
                .writer(userId)
                .build();
    }

    public void updateHub(HubRequestDto dto, BigDecimal latitude, BigDecimal longitude) {
        if(dto.getHubName() != null && !dto.getHubName().isBlank()) {
            this.hubName = dto.getHubName();
        }
        if(dto.getAddress() != null && !dto.getAddress().isBlank()) {
            this.address = dto.getAddress();
            this.latitude = latitude;
            this.longitude = longitude;
        }
        if(dto.getProvince() != null && !dto.getProvince().isBlank()) {
            this.province = dto.getProvince();
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

    // 유효성 검사
    private static void requiredNonBlank(String s, String f){
        if(s == null || s.isBlank()){
            throw new BusinessException(ErrorCode.INVALID_INPUT, f+" 칸을 입력해주세요.");
        }
    }
}
