package com.ojo.mullyuojo.hub.domain;

import com.ojo.mullyuojo.hub.application.dtos.HubSearchDto;
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

import static com.ojo.mullyuojo.hub.domain.QCompanyList.companyList;
import static com.ojo.mullyuojo.hub.domain.QDeliveryManagerList.deliveryManagerList;
import static com.ojo.mullyuojo.hub.domain.QHub.hub;

@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Hub> searchHubs(HubSearchDto searchDto, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(hubNameContains(searchDto.getHubName()));
        builder.and(addressContains(searchDto.getAddress()));
        builder.and(provinceEq(searchDto.getProvince()));

        // id 검색
        if (searchDto.getId() != null) {
            builder.and(hub.id.eq(searchDto.getId()));
        }

        // 업체 ID 조건 (서브쿼리)
        if (searchDto.getCompanyId() != null) {
            builder.and(hub.id.in(
                    JPAExpressions.select(companyList.hub.id)
                            .from(companyList)
                            .where(companyList.id.eq(searchDto.getCompanyId()))
            ));
        }

        // 업체명 조건 (서브쿼리)
        if (searchDto.getCompanyName() != null && !searchDto.getCompanyName().isBlank()) {
            builder.and(hub.id.in(
                    JPAExpressions.select(companyList.hub.id)
                            .from(companyList)
                            .where(companyList.name.containsIgnoreCase(searchDto.getCompanyName()))
            ));
        }

        // 배송 담당자 ID 조건 (서브쿼리)
        if (searchDto.getDeliveryManagerId() != null) {
            builder.and(hub.id.in(
                    JPAExpressions.select(deliveryManagerList.hub.id)
                            .from(deliveryManagerList)
                            .where(deliveryManagerList.id.eq(searchDto.getDeliveryManagerId()))
            ));
        }

        // 배송 담당자명 조건 (서브쿼리)
        if (searchDto.getDeliveryManagerName() != null && !searchDto.getDeliveryManagerName().isBlank()) {
            builder.and(hub.id.in(
                    JPAExpressions.select(deliveryManagerList.hub.id)
                            .from(deliveryManagerList)
                            .where(deliveryManagerList.name.containsIgnoreCase(searchDto.getDeliveryManagerName()))
            ));
        }

        // JPAQuery 객체 생성
        JPAQuery<Hub> query = queryFactory
                .selectFrom(hub)
                .leftJoin(hub.companyLists).fetchJoin()
//                .leftJoin(hub.deliveryManagerLists).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // 정렬 적용
        @SuppressWarnings("unchecked")
        List<OrderSpecifier<?>> orderSpecifiersList = getAllOrderSpecifiers(pageable);
        if (!orderSpecifiersList.isEmpty()) {
            query.orderBy(orderSpecifiersList.toArray(new OrderSpecifier[0]));
        }

        // 조회 실행
        List<Hub> hubEntities = query.fetch();

        // 전체 카운트 조회
        Long total = queryFactory
                .select(hub.count())
                .from(hub)
                .where(builder)
                .fetchOne();
        long totalCount = (total != null) ? total : 0L;

        return new PageImpl<>(hubEntities, pageable, totalCount);
    }

    private BooleanExpression hubNameContains(String hubName) {
        return hubName != null && !hubName.isBlank() ? hub.hubName.containsIgnoreCase(hubName) : null;
    }

    private BooleanExpression addressContains(String address) {
        return address != null && !address.isBlank() ? hub.address.containsIgnoreCase(address) : null;
    }

    private BooleanExpression provinceEq(String province) {
        return province != null && !province.isBlank() ? hub.province.eq(province) : null;
    }

    public List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        if (pageable.getSort() != null) {
            for (Sort.Order sortOrder : pageable.getSort()) {
                com.querydsl.core.types.Order direction = sortOrder.isAscending() ?
                        com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;
                switch (sortOrder.getProperty()) {
                    case "hubName":
                        orders.add(new OrderSpecifier<>(direction, hub.hubName));
                        break;
                    case "province":
                        orders.add(new OrderSpecifier<>(direction, hub.province));
                        break;
                    case "createdAt":
                        orders.add(new OrderSpecifier<>(direction, hub.createdAt));
                        break;
                    default:
                        break;
                }
            }
        }
        return orders;
    }
}
