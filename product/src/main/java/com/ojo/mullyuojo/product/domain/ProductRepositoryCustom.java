package com.ojo.mullyuojo.product.domain;

import com.ojo.mullyuojo.product.application.dtos.ProductResponseDto;
import com.ojo.mullyuojo.product.application.dtos.ProductSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepositoryCustom {
    Page<ProductResponseDto> searchProducts(ProductSearchDto searchDto, Pageable pageable);

}
