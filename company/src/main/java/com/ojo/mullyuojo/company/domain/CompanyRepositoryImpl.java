package com.ojo.mullyuojo.company.domain;

import com.ojo.mullyuojo.company.application.dtos.CompanyResponseDto;
import com.ojo.mullyuojo.company.application.dtos.CompanySearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.ojo.mullyuojo.company.domain.QCompany.company;

@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CompanyResponseDto> searchCompanies(CompanySearchDto searchDto, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        //Id 조회
        if(searchDto.getId() != null){
            builder.and(company.id.eq(searchDto.getId()));
        }
        if(searchDto.getCompanyId() != null){
            builder.and(company.companyId.eq(searchDto.getCompanyId()));
        }
        if(searchDto.getHubId() != null){
            builder.and(company.hubId.eq(searchDto.getHubId()));
        }
        if(searchDto.getProductId() != null){
            builder.and(company.productId.eq(searchDto.getProductId()));
        }

        //검색어로 찾기
        builder.and(companyNameContains(searchDto.getName()));
        builder.and(addressContains(searchDto.getAddress()));

        //실제조회
        List<CompanyResponseDto> companies = queryFactory
                .select(Projections.constructor(CompanyResponseDto.class,
                        company.id,
                        company.companyId,
                        company.hubId,
                        company.productId,
                        company.type,
                        company.name,
                        company.address,
                        company.writer
                        ))
                .from(company)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        //전체 카운트
        Long total = queryFactory
                .select(company.count())
                .from(company)
                .where(builder)
                .fetchOne();
        long totalCount = (total != null) ? total : 0L;

        return new PageImpl<>(companies, pageable, totalCount);
    }

    private BooleanExpression companyNameContains(String companyName) {
        return companyName != null && !companyName.isBlank() ? company.name.containsIgnoreCase(companyName) : null;
    }

    private BooleanExpression addressContains(String address) {
        return address != null && !address.isBlank() ? company.address.containsIgnoreCase(address) : null;
    }

    public List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        if(pageable.getSort() != null){
            for(Sort.Order sortOrder : pageable.getSort()){
                com.querydsl.core.types.Order direction = sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;
                switch (sortOrder.getProperty()){
                    case "companyName":
                        orders.add(new OrderSpecifier<>(direction, company.name));
                        break;
                    case "hubId":
                        orders.add(new OrderSpecifier<>(direction, company.hubId));
                        break;
                    default:
                        break;
                }
            }
        }
        return orders;
    }
}
