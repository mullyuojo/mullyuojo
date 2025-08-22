package com.ojo.mullyuojo.product.controller;

import com.ojo.mullyuojo.product.application.ProductService;
import com.ojo.mullyuojo.product.application.dtos.ProductRequestDto;
import com.ojo.mullyuojo.product.application.dtos.ProductResponseDto;
import com.ojo.mullyuojo.product.application.dtos.ProductSearchDto;
import com.ojo.mullyuojo.product.application.security.AccessContext;
import com.ojo.mullyuojo.product.application.security.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @ModelAttribute
    public AccessContext setAccessContext(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Role") Role role,
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-Hub-Id") Long hubId
    ) {
        return new AccessContext(userId, role, companyId, hubId);
    }

    //목록 검색
    @PostMapping("/search")
    public ResponseEntity<Page<ProductResponseDto>> getProducts(@RequestBody ProductSearchDto searchDto,
                                                                Pageable pageable,
                                                                AccessContext ctx)
    {
        return ResponseEntity.ok(productService.getProducts(searchDto, pageable, ctx));
    }

    //단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id,
                                                         AccessContext ctx) {
        return ResponseEntity.ok(productService.getProductById(id, ctx));
    }

    //상품 생성
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto,
                                                            AccessContext ctx){
        return ResponseEntity.ok(productService.createProduct(productRequestDto, ctx.getUserId(),ctx));
    }

    //상품 업데이트
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id,
                                                            @RequestBody ProductRequestDto productRequestDto,
                                                            AccessContext ctx){
        return ResponseEntity.ok(productService.updateProduct(id, productRequestDto, ctx.getUserId(), ctx));
    }

    //상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id,
                                              AccessContext ctx){
        productService.deleteProduct(id, ctx.getUserId(), ctx);
        return ResponseEntity.noContent().build();
    }

    // 재고 차감

    @PostMapping("/{id}/reduce-stock")
    public ResponseEntity<Void> reduceStock (@PathVariable Long id,
                                             @RequestParam Integer quantity,
                                             AccessContext ctx){
        productService.reduceStock(id, quantity, ctx);
        return ResponseEntity.noContent().build();
    }

}
