package com.ojo.mullyuojo.company.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ojo.mullyuojo.company.application.dtos.CompanyRequestDto;
import com.ojo.mullyuojo.company.application.dtos.CompanyResponseDto;
<<<<<<< HEAD
=======
import com.ojo.mullyuojo.company.application.dtos.manager.CompanyManagerDto;
import com.ojo.mullyuojo.company.application.dtos.product.CompanyProductDto;
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
import com.ojo.mullyuojo.company.application.exception.BusinessException;
import com.ojo.mullyuojo.company.application.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
<<<<<<< HEAD
=======
import org.hibernate.annotations.BatchSize;
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
<<<<<<< HEAD
=======
import java.util.ArrayList;
import java.util.List;
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
<<<<<<< HEAD
@Table(name = "companies", indexes = { @Index(name = "idx_companies_name", columnList = "name")})
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE companies SET deleted_at = now(), deleted_by = ? WHERE id = ?")
=======
@Table(name = "company", indexes = { @Index(name = "idx_company_name", columnList = "name")})
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE company SET deleted_at = now(), deleted_by = ? WHERE id = ?")
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
@Where(clause = "deleted_at IS NULL")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
<<<<<<< HEAD
    Long id;

    @Column(nullable = true)
    private Long companyId;

    @Column(nullable = true)
    private Long hubId;

    @Column(nullable = true)
    private Long productId;

=======
    @Column(name = "company_id")
    Long id;

    @Column(nullable = true)
    private Long hubId;

>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
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

<<<<<<< HEAD
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
=======
    // 상품 담당자 테이블
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @BatchSize(size = 10)
    private List<CompanyProduct> products = new ArrayList<>();

    //업체 담당자 리스트 테이블
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private List<CompanyManager> managers = new ArrayList<>();


    public static Company createCompany(CompanyRequestDto dto, String userId) {
        requiredNonBlank(dto.getName(), "company name");
        requiredNonBlank(dto.getAddress(), "company address");

        // Builder 객체 생성
        Company.CompanyBuilder builder = Company.builder()
                .name(dto.getName().trim())
                .address(dto.getAddress().trim())
                .hubId(dto.getHubId())
                .type(dto.getType())
                .writer(userId);

        // Company 엔티티 생성
        Company company = builder.build();

        // DTO -> 엔티티 변환 후 리스트 세팅
        if (dto.getProducts() != null && !dto.getProducts().isEmpty()) {
            List<CompanyProduct> productEntities = new ArrayList<>();
            for (CompanyProductDto dtoProduct : dto.getProducts()) {
                CompanyProduct product = new CompanyProduct();
                product.setName(dtoProduct.getName());
                product.setCompany(company); // 연관관계 설정
                productEntities.add(product);
            }
            company.setProducts(productEntities);
        }

        if (dto.getManagers() != null && !dto.getManagers().isEmpty()) {
            List<CompanyManager> managerEntities = new ArrayList<>();
            for (CompanyManagerDto dtoManager : dto.getManagers()) {
                CompanyManager manager = new CompanyManager();
                manager.setName(dtoManager.getName());
                manager.setSlackId(dtoManager.getSlackId());
                manager.setCompany(company); // 연관관계 설정
                managerEntities.add(manager);
            }
            company.setManagers(managerEntities);
        }
        return company;
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
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

<<<<<<< HEAD
        if(dto.getCompanyId()!=null){
            if(dto.getCompanyId() <=0){
                throw new BusinessException(ErrorCode.INVALID_INPUT,"업체ID는 1이상의 숫자여야 합니다.");
            }
            if(!dto.getCompanyId().equals(this.companyId)){
                this.companyId = dto.getCompanyId();
            }
        }

=======
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
        if(dto.getHubId()!=null){
            if(dto.getHubId() <=0){
                throw new BusinessException(ErrorCode.INVALID_INPUT,"허브ID는 1이상의 숫자여야 합니다.");
            }
            if(!dto.getHubId().equals(this.hubId)){
                this.hubId = dto.getHubId();
            }
        }
<<<<<<< HEAD

        if(dto.getProductId() != null){
            if(dto.getProductId() <=0){
                throw new BusinessException(ErrorCode.INVALID_INPUT,"제품ID는 1 이상의 숫자여야 합니다.");
            }
            if(!dto.getProductId().equals(this.productId)){
                this.productId = dto.getProductId();
            }
        }
=======
        // products 리스트가 존재하면 업데이트
        if (dto.getProducts() != null) {
            // 기존 엔티티 리스트 초기화
            this.products.clear();
            // DTO를 엔티티로 변환 후 추가
            dto.getProducts().forEach(dtoProduct -> {
                CompanyProduct product = new CompanyProduct();
                product.setName(dtoProduct.getName());
                product.setCompany(this); // 연관관계 재설정
                this.products.add(product);
            });
        }

        // managers 리스트가 존재하면 업데이트
        if (dto.getManagers() != null) {
            this.managers.clear();
            dto.getManagers().forEach(dtoManager -> {
                CompanyManager manager = new CompanyManager();
                manager.setName(dtoManager.getName());
                manager.setSlackId(dtoManager.getSlackId());
                manager.setCompany(this);
                this.managers.add(manager);
            });
        }

>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
        this.updatedAt = LocalDateTime.now();
    }


    public void deleteCompany(String deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

    public CompanyResponseDto toResponseDto() {
<<<<<<< HEAD
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
=======
        return CompanyResponseDto.builder()
                .id(this.id)
                .hubId(this.hubId)
                .type(this.type)
                .name(this.name)
                .address(this.address)
                .writer(this.writer)
                // Product DTO 변환
                .products(this.products != null
                        ? this.products.stream()
                        .map(p -> new CompanyProductDto(p.getId(), p.getName()))
                        .toList()
                        : new ArrayList<>())
                // Manager DTO 변환
                .managers(this.managers != null
                        ? this.managers.stream()
                        .map(m -> new CompanyManagerDto(m.getId(), m.getName(), m.getSlackId()))
                        .toList()
                        : new ArrayList<>())
                .build();
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
    }
    // 유효성 검사
    private static void requiredNonBlank(String s, String f){
        if(s==null || s.isBlank()){
            throw new BusinessException(ErrorCode.INVALID_INPUT, f+" 칸을 입력해주세요");
        }
    }

}
