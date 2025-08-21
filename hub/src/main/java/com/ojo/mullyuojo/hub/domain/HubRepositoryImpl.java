package com.ojo.mullyuojo.hub.domain;

import com.ojo.mullyuojo.hub.application.dtos.HubResponseDto;
import com.ojo.mullyuojo.hub.application.dtos.HubSearchDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ojo.mullyuojo.hub.domain.QHub.hub;

@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<HubResponseDto> searchHubs(HubSearchDto dto, Pageable pageable) {
        List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);

        List<Hub> hubs = queryFactory
                .selectFrom(hub)
                .where(
                        hub.deletedAt.isNull(),
                        hubNameContains(dto.getHubName()),
                        addressContains(dto.getAddress()),
                        provinceEq(dto.getProvince())
                )
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(hub.count())
                .from(hub)
                .where(
                        hub.deletedAt.isNull(),
                        hubNameContains(dto.getHubName()),
                        addressContains(dto.getAddress()),
                        provinceEq(dto.getProvince())
                )
                .fetchOne();
        long totalCount = (total == null) ? 0L : total;

        List<HubResponseDto> results = hubs.stream()
                .map(HubResponseDto::from)
                .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, totalCount);
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
        if(pageable.getSort() != null) {
            for(Sort.Order sortOrder : pageable.getSort()) {
                com.querydsl.core.types.Order direction = sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;
                switch(sortOrder.getProperty()) {
                    case "createdAt":
                        orders.add(new OrderSpecifier<>(direction, hub.createdAt));
                        break;
                    case "hubName":
                        orders.add(new OrderSpecifier<>(direction, hub.hubName));
                        break;
                    case "province":
                        orders.add(new OrderSpecifier<>(direction, hub.province));
                        break;
                    default:
                        break;
                }
            }
        }
        return orders;
    }

}
