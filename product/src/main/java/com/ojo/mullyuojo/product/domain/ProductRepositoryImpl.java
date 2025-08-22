package com.ojo.mullyuojo.product.domain;

import com.ojo.mullyuojo.product.application.dtos.ProductResponseDto;
import com.ojo.mullyuojo.product.application.dtos.ProductSearchDto;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ojo.mullyuojo.product.domain.QProduct.product;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<ProductResponseDto> searchProducts(ProductSearchDto searchDto, Pageable pageable) {
        List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);

        List<Product> products = queryFactory
                .selectFrom(product)
                .where(
                        nameContains(searchDto.getName()),
                        priceBetween(searchDto.getMinPrice(), searchDto.getMaxPrice()),
                        stockBetween(searchDto.getMinStock(), searchDto.getMaxStock()),
                        descriptionContains(searchDto.getDescription())
                )
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset()) // n개 건너뛰기
                .limit(pageable.getPageSize()) // m개 가져오기
                .fetch(); // 데이터 목록 가져옴

        Long total = queryFactory
                .select(product.count())
                .from(product)
                .where(
                        nameContains(searchDto.getName()),
                        priceBetween(searchDto.getMinPrice(), searchDto.getMaxPrice()),
                        stockBetween(searchDto.getMinStock(), searchDto.getMaxStock()),
                        descriptionContains(searchDto.getDescription())
                )
                .fetchOne();
        long totalCount = (total == null) ? 0L : total;
        List<ProductResponseDto> results = products.stream()
                .map(Product::toResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<ProductResponseDto>(results, pageable, totalCount);
    }


    private BooleanExpression nameContains(String name) {
        return (name != null && !name.isBlank()) ? product.name.containsIgnoreCase(name) : null;
    }

    private BooleanExpression descriptionContains(String description){
        return (description != null && !description.isBlank()) ? product.description.containsIgnoreCase(description) : null;
    }

    private BooleanExpression priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        if(minPrice != null && maxPrice != null){
            return product.price.between(minPrice, maxPrice);
        } else if(minPrice != null){
            return product.price.goe(minPrice); // goe(): 크거나 같다
        } else if(maxPrice !=null){
            return product.price.loe(maxPrice); // loe(): 작거나 같다
        }else {
            return null;
        }
    }

    private BooleanExpression stockBetween(Integer minStock, Integer maxStock){
        if(minStock != null && maxStock != null){
            return product.stock.between(minStock, maxStock);
        } else if(minStock != null){
            return product.stock.goe(minStock);
        } else if(maxStock != null){
            return product.stock.loe(maxStock);
        } else {
            return null;
        }
    }


    private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
           // OrderSpecifier: QueryDSL에서 order by절을 만들 때 쓰는 '정렬조건'
           // OrderSpecifier: pageable객체에 들어있는 정렬 조건들을 QueryDSL에서 사용할 수 있는 정렬조건 OrderSpecifier 목록
           // 으로 변환해서 orders 리스트에 담는 코드. 여기에서 order은 queryDSL에서 가져온 enum클래스
           // Sort.Order -> QueryDSL의 OrderSpecifier변환 과정
        if(pageable.getSort() !=null){
            for (Sort.Order sortOrder: pageable.getSort()) {
                com.querydsl.core.types.Order direction = sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;
                switch (sortOrder.getProperty()) {
                    case "name":
                        orders.add(new OrderSpecifier<>(direction, product.name));
                        break;
                    case "price":
                        orders.add(new OrderSpecifier<>(direction, product.price));
                        break;
                    case "stock":
                        orders.add(new OrderSpecifier<>(direction, product.stock));
                        break;
                    case "createdAt":
                        orders.add(new OrderSpecifier<>(direction, product.createdAt));
                        break;
                    default:
                        break;
                }
            }
        }
        return orders;
    }
}
