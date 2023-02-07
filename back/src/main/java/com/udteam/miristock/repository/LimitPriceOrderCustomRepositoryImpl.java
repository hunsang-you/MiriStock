package com.udteam.miristock.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udteam.miristock.entity.Deal;
import com.udteam.miristock.entity.LimitPriceOrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.udteam.miristock.entity.QLimitPriceOrderEntity.limitPriceOrderEntity;

@Repository
@RequiredArgsConstructor
public class LimitPriceOrderCustomRepositoryImpl implements LimitPriceOrderCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    private BooleanExpression isLimitPriceOrderTypeNull(Deal limitPriceOrderType){
        if(limitPriceOrderType == null) {
            return null;
        } else {
            return limitPriceOrderEntity.limitPriceOrderType.eq(limitPriceOrderType);
        }
    }

    @Override
    public List<LimitPriceOrderEntity> findAllByMemberNoAndLimitPriceOrderType(Integer memberNo, Deal limitPriceOrderType) {
        JPAQuery<LimitPriceOrderEntity> query = jpaQueryFactory
                .selectFrom(limitPriceOrderEntity)
                .where(limitPriceOrderEntity.memberNo.eq(memberNo)
                        .and(isLimitPriceOrderTypeNull(limitPriceOrderType))
                ).fetchAll();
        return query.fetch();
    }
}
