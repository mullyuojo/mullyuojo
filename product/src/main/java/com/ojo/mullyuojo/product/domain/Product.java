package com.ojo.mullyuojo.product.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ojo.mullyuojo.product.application.dtos.ProductRequestDto;
import com.ojo.mullyuojo.product.application.dtos.ProductResponseDto;
import com.ojo.mullyuojo.product.application.exception.BusinessException;
import com.ojo.mullyuojo.product.application.exception.ErrorCode;
import com.ojo.mullyuojo.product.application.security.AccessContext;
import com.ojo.mullyuojo.product.application.security.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products", indexes = { @Index(name = "idx_products_name", columnList = "name")})
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE products SET deleted_at = now(), deleted_by = ? WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, precision = 15)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @CreatedBy
    @Column(name = "created_by", nullable = false)
    private String writer;

    @Column(length = 150)
    private String description;

    @Column(nullable = false)
    private Long companyId;

    @Column(nullable = false)
    private Long hubId;


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

    public static Product createProduct(ProductRequestDto dto, String userId){
        requiredNonBlank(dto.getName(), "name");
        requiredNonNull(dto.getPrice(), "price");
        requiredNonNull(dto.getStock(), "stock");
        validatePrice(dto.getPrice());
        validateStock(dto.getStock());

        return Product.builder()
                .name(dto.getName().trim())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .description(dto.getDescription())
                .writer(userId)
                .hubId(dto.getHubId())
                .companyId(dto.getCompanyId())
                .build();
    }

    public void updateProduct(ProductRequestDto dto) {
        if (dto.getName() != null && !dto.getName().isBlank()) {
            String trimmed = dto.getName().trim();
            if(trimmed.isEmpty()){
                throw new BusinessException(ErrorCode.INVALID_INPUT,"제품명을 공백으로 입력할 수 없습니다.");
            }
            if(!trimmed.equals(this.name)){
                this.name = trimmed;
            }
        }

        if(dto.getDescription() != null && !Objects.equals(dto.getDescription() , this.description)){
            this.description = dto.getDescription() ;
        }

        if(dto.getPrice() != null){
            requiredNonNull(dto.getPrice(), "price");
            validatePrice(dto.getPrice());
            this.price = dto.getPrice();
        }

        if(dto.getStock() != null){
            requiredNonNull(dto.getStock(), "stock");
            validateStock(dto.getStock());
            if(!dto.getStock().equals(this.stock)){
                this.stock = dto.getStock();
            }
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void deleteProduct(String deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

    public ProductResponseDto toResponseDto() {
        return new ProductResponseDto(
                this.id,
                this.name,
                this.price,
                this.stock,
                this.description,
                this.writer,
                this.companyId,
                this.hubId,
                this.createdAt,
                this.updatedAt
        );
    }

    public void reduceStock(Integer quantity) {
        if ( quantity == null || quantity <= 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "수량은 1이상 입력해야 합니다.");
        }
        if(this.stock < quantity) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK, "재고 수량이 적어도 1개 이상되어야 합니다.");
        }
        this.stock -= quantity;
    }

    // 유효성 검사

    private static void validatePrice(BigDecimal price) {
        if(price.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,"가격은 음수를 입력할 수 없습니다.");
        }
        if(price.remainder(BigDecimal.TEN).compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,"가격은 10원 단위로 입력할 수 있습니다.");
        }
    }

    private static void validateStock(Integer stock) {
        if(stock <= 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,"재고 수량은 1이상 입력 가능합니다.");
        }
    }

    private static void requiredNonNull(Object o, String f) {
        if(o==null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,f+" 칸을 입력해주세요.");
        }
    }

    // String 에서만 공백 개념이 있으므로 메서드를 따로 만들어준다
    private static void requiredNonBlank(String s, String f) {
        if(s==null || s.isBlank()){
            throw new BusinessException(ErrorCode.INVALID_INPUT,f + " 칸을 입력해주세요");
        }
    }
}
