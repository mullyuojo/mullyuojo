package com.ojo.mullyuojo.company.domain;

<<<<<<< HEAD
import com.ojo.mullyuojo.company.application.dtos.CompanyResponseDto;
import com.ojo.mullyuojo.company.application.dtos.CompanySearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
=======
import com.ojo.mullyuojo.company.application.dtos.CompanySearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.ojo.mullyuojo.company.domain.QCompany.company;
<<<<<<< HEAD
=======
import static com.ojo.mullyuojo.company.domain.QCompanyManager.companyManager;
import static com.ojo.mullyuojo.company.domain.QCompanyProduct.companyProduct;
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139

@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
<<<<<<< HEAD
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
=======
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

        // 담당자 ID 조건
        if (searchDto.getManagerId() != null) {
            builder.and(company.id.in(
                    JPAExpressions.select(companyManager.company.id)  // JPAExpressions: QueryDSL에서 서브쿼리(Subquery)
                            .from(companyManager)
                            .where(companyManager.id.eq(searchDto.getManagerId()))
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
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
        Long total = queryFactory
                .select(company.count())
                .from(company)
                .where(builder)
                .fetchOne();
        long totalCount = (total != null) ? total : 0L;

<<<<<<< HEAD
        return new PageImpl<>(companies, pageable, totalCount);
=======
        return new PageImpl<>(companyEntities, pageable, totalCount);
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
    }

    private BooleanExpression companyNameContains(String companyName) {
        return companyName != null && !companyName.isBlank() ? company.name.containsIgnoreCase(companyName) : null;
    }

    private BooleanExpression addressContains(String address) {
        return address != null && !address.isBlank() ? company.address.containsIgnoreCase(address) : null;
    }

<<<<<<< HEAD
    public List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
=======
    public List<OrderSpecifier<?>> getAllOrderSpecifiers (Pageable pageable) {
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        if(pageable.getSort() != null){
            for(Sort.Order sortOrder : pageable.getSort()){
                com.querydsl.core.types.Order direction = sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;
                switch (sortOrder.getProperty()){
<<<<<<< HEAD
                    case "companyName":
=======
                    case "name":
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
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
