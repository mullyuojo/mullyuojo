package com.ojo.mullyuojo.company.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ojo.mullyuojo.company.application.dtos.CompanyRequestDto;
import com.ojo.mullyuojo.company.application.dtos.CompanyResponseDto;
import com.ojo.mullyuojo.company.application.exception.BusinessException;
import com.ojo.mullyuojo.company.application.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "companies", indexes = { @Index(name = "idx_companies_name", columnList = "name")})
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE companies SET deleted_at = now(), deleted_by = ? WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = true)
    private Long companyId;

    @Column(nullable = true)
    private Long hubId;

    @Column(nullable = true)
    private Long productId;

    @Column
    private CompanyType type;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String address;

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

    @Version  //낙관적 락 구현(데이터 충돌=덮어쓰기 방지)
    private Long version;

    public static Company createCompany(CompanyRequestDto dto, String userId) {
        requiredNonBlank(dto.getName(),"company name");
        requiredNonBlank(dto.getAddress(),"company address");
        return com.ojo.mullyuojo.company.domain.Company.builder()
                .name(dto.getName().trim())
                .address(dto.getAddress().trim())
                .companyId(dto.getCompanyId())
                .hubId(dto.getHubId())
                .productId(dto.getProductId())
                .type(dto.getType())
                .writer(userId)
                .build();
    }

    public void updateCompany(CompanyRequestDto dto) {
        if(dto.getName() != null && !dto.getName().isBlank()){
            String trimmedName = dto.getName().trim();
            if(trimmedName.isEmpty()){
                throw new BusinessException(ErrorCode.INVALID_INPUT,"업체명을 공백으로 입력할 수 없습니다");
            }
            if(!trimmedName.equals(this.name)){
                this.name = trimmedName;
            }
        }
        if(dto.getAddress() != null && !dto.getAddress().isBlank()){
            String trimmedAddress = dto.getAddress().trim();
            if(trimmedAddress.isEmpty()){
                throw new BusinessException(ErrorCode.INVALID_INPUT,"업체 주소를 공백으로 입력할 수 없습니다");
            }
            if(!trimmedAddress.equals(this.address)){
                this.address = trimmedAddress;
            }
        }

        if(dto.getType() != null && dto.getType() !=this.type){
            this.type = dto.getType();
        }

        if(dto.getCompanyId()!=null){
            if(dto.getCompanyId() <=0){
                throw new BusinessException(ErrorCode.INVALID_INPUT,"업체ID는 1이상의 숫자여야 합니다.");
            }
            if(!dto.getCompanyId().equals(this.companyId)){
                this.companyId = dto.getCompanyId();
            }
        }

        if(dto.getHubId()!=null){
            if(dto.getHubId() <=0){
                throw new BusinessException(ErrorCode.INVALID_INPUT,"허브ID는 1이상의 숫자여야 합니다.");
            }
            if(!dto.getHubId().equals(this.hubId)){
                this.hubId = dto.getHubId();
            }
        }

        if(dto.getProductId() != null){
            if(dto.getProductId() <=0){
                throw new BusinessException(ErrorCode.INVALID_INPUT,"제품ID는 1 이상의 숫자여야 합니다.");
            }
            if(!dto.getProductId().equals(this.productId)){
                this.productId = dto.getProductId();
            }
        }
        this.updatedAt = LocalDateTime.now();
    }


    public void deleteCompany(String deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

    public CompanyResponseDto toResponseDto() {
        return new CompanyResponseDto(
                this.id,
                this.companyId,
                this.hubId,
                this.productId,
                this.type,
                this.name,
                this.address,
                this.writer
        );
    }
    // 유효성 검사
    private static void requiredNonBlank(String s, String f){
        if(s==null || s.isBlank()){
            throw new BusinessException(ErrorCode.INVALID_INPUT, f+" 칸을 입력해주세요");
        }
    }

}
