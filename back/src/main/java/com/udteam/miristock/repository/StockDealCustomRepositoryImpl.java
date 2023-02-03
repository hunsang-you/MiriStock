package com.udteam.miristock.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udteam.miristock.entity.Deal;
import com.udteam.miristock.entity.StockDealEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.udteam.miristock.entity.QStockDealEntity.stockDealEntity;

@Repository
@RequiredArgsConstructor
public class StockDealCustomRepositoryImpl implements StockDealCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;


    private BooleanExpression isStockDealTypeNull(Deal stockDealType){
        if(stockDealType == null) {
            return null;
        } else {
            return stockDealEntity.stockDealType.eq(stockDealType);
        }
    }
    @Override
    public List<StockDealEntity> findAllByMemberNoAndStockDealType(Integer memberNo, Deal stockDealType) {
        JPAQuery<StockDealEntity> query = jpaQueryFactory
                .selectFrom(stockDealEntity)
                .where(stockDealEntity.memberNo.eq(memberNo)
                        .and(isStockDealTypeNull(stockDealType))
                ).fetchAll();
        return query.fetch();
    }
}
