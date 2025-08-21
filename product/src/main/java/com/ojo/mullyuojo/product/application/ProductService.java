package com.ojo.mullyuojo.product.application;

import com.ojo.mullyuojo.product.application.dtos.ProductRequestDto;
import com.ojo.mullyuojo.product.application.dtos.ProductResponseDto;
import com.ojo.mullyuojo.product.application.dtos.ProductSearchDto;
import com.ojo.mullyuojo.product.application.exception.BusinessException;
import com.ojo.mullyuojo.product.application.exception.ErrorCode;
import com.ojo.mullyuojo.product.application.security.AccessContext;
import com.ojo.mullyuojo.product.application.security.AccessGuard;
import com.ojo.mullyuojo.product.application.security.Action;
import com.ojo.mullyuojo.product.application.security.ResourceScope;
import com.ojo.mullyuojo.product.domain.Product;
import com.ojo.mullyuojo.product.domain.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto, String userId, AccessContext ctx) {
        AccessGuard.requiredPermission(Action.CREATE, ctx,
                ResourceScope.of(productRequestDto.getHubId(), productRequestDto.getCompanyId()));

        Product product = Product.createProduct(productRequestDto, userId);

        Product savedProduct = productRepository.save(product);
        return toResponseDto(savedProduct);
    }

    public Page<ProductResponseDto> getProducts(ProductSearchDto searchDto, Pageable pageable, AccessContext ctx) {
        AccessGuard.requiredPermission(
                Action.READ,
                ctx,
                ResourceScope.of(ctx.getHubId(), ctx.getCompanyId())
        );
        return productRepository.searchProducts(searchDto, pageable);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long productId, AccessContext ctx) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "찾으시는 제품이 존재하지 않습니다."));
        AccessGuard.requiredPermission(
                Action.READ,
                ctx,
                ResourceScope.of(product.getHubId(), product.getCompanyId())
        );
        return toResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long productId, ProductRequestDto productRequestDto, String userId, AccessContext ctx) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "찾으시는 제품이 존재하지 않습니다."));

        AccessGuard.requiredPermission(Action.UPDATE,
                ctx,
                ResourceScope.of(product.getHubId(), product.getCompanyId()));

        product.updateProduct(productRequestDto);

        return toResponseDto(product);
    }

    @Transactional
    public void deleteProduct(Long productId, String userId, AccessContext ctx) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "찾으시는 제품이 존재하지 않습니다."));

        AccessGuard.requiredPermission(Action.DELETE,
                ctx,
                ResourceScope.of(product.getHubId(), product.getCompanyId()));

        product.deleteProduct(userId);
    }

    @Transactional
    public void reduceStock(Long productId, Integer quantity, AccessContext ctx) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "찾으시는 "+ productId+ " 제품이 존재하지 않습니다."));
        AccessGuard.requiredPermission(Action.UPDATE,
                ctx,
                ResourceScope.of(ctx.getHubId(), ctx.getCompanyId()));
        product.reduceStock(quantity);
        product.setUpdatedAt(LocalDateTime.now());
    }

    private ProductResponseDto toResponseDto(Product product){
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getDescription(),
                product.getWriter(),
                product.getCompanyId(),
                product.getHubId(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
