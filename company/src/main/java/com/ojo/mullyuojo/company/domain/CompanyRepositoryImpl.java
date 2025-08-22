package com.ojo.mullyuojo.company.domain;

import com.ojo.mullyuojo.company.application.dtos.CompanySearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.ojo.mullyuojo.company.domain.QCompany.company;
import static com.ojo.mullyuojo.company.domain.QCompanyManager.companyManager;
import static com.ojo.mullyuojo.company.domain.QCompanyProduct.companyProduct;

@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Company> searchCompanies(CompanySearchDto searchDto, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        // id 검색
        if (searchDto.getId() != null) {
            builder.and(company.id.eq(searchDto.getId()));
        }
        if (searchDto.getHubId() != null) {
            builder.and(company.hubId.eq(searchDto.getHubId()));
        }
        if (searchDto.getType() != null) {
            builder.and(company.type.eq(searchDto.getType()));
        }
        if (searchDto.getName() != null && !searchDto.getName().isBlank()) {
            builder.and(company.name.containsIgnoreCase(searchDto.getName()));
        }
        if (searchDto.getAddress() != null && !searchDto.getAddress().isBlank()) {
            builder.and(company.address.containsIgnoreCase(searchDto.getAddress()));
        }
        if (searchDto.getWriter() != null && !searchDto.getWriter().isBlank()) {
            builder.and(company.writer.containsIgnoreCase(searchDto.getWriter()));
        }

        // 상품 ID 조건
        if (searchDto.getProductId() != null) {
            builder.and(company.id.in(
                    JPAExpressions.select(companyProduct.company.id)
                            .from(companyProduct)
                            .where(companyProduct.id.eq(searchDto.getProductId()))
            ));
        }
        if (searchDto.getProductName() != null && !searchDto.getProductName().isBlank()) {
            builder.and(company.id.in(
                    JPAExpressions.select(companyProduct.company.id)
                            .from(companyProduct)
                            .where(companyProduct.name.containsIgnoreCase(searchDto.getProductName()))
            ));
        }

        // 담당자 ID 조건
        if (searchDto.getManagerId() != null) {
            builder.and(company.id.in(
                    JPAExpressions.select(companyManager.company.id)  // JPAExpressions: QueryDSL에서 서브쿼리(Subquery)
                            .from(companyManager)
                            .where(companyManager.id.eq(searchDto.getManagerId()))
            ));
        }
        if (searchDto.getManagerName() != null && !searchDto.getManagerName().isBlank()) {
            builder.and(company.id.in(
                    JPAExpressions.select(companyManager.company.id)
                            .from(companyManager)
                            .where(companyManager.name.containsIgnoreCase(searchDto.getManagerName()))
            ));
        }


        // JPAQuery 객체 생성
        JPAQuery<Company> query = queryFactory
                .selectFrom(company)
                .leftJoin(company.products).fetchJoin()
//                .leftJoin(company.managers).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // 정렬 적용
        @SuppressWarnings("unchecked")
        List<OrderSpecifier<?>> orderSpecifiersList = getAllOrderSpecifiers(pageable);
        if (!orderSpecifiersList.isEmpty()) {
            query.orderBy(orderSpecifiersList.toArray(new OrderSpecifier[0]));
        }

        // 결과를 엔티티 리스트로 받음
        List<Company> companyEntities = query.fetch();

        // 전체 카운트 조회
        Long total = queryFactory
                .select(company.count())
                .from(company)
                .where(builder)
                .fetchOne();
        long totalCount = (total != null) ? total : 0L;

        return new PageImpl<>(companyEntities, pageable, totalCount);
    }

    private BooleanExpression companyNameContains(String companyName) {
        return companyName != null && !companyName.isBlank() ? company.name.containsIgnoreCase(companyName) : null;
    }

    private BooleanExpression addressContains(String address) {
        return address != null && !address.isBlank() ? company.address.containsIgnoreCase(address) : null;
    }

    public List<OrderSpecifier<?>> getAllOrderSpecifiers (Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        if(pageable.getSort() != null){
            for(Sort.Order sortOrder : pageable.getSort()){
                com.querydsl.core.types.Order direction = sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;
                switch (sortOrder.getProperty()){
                    case "name":
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
