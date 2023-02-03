package com.udteam.miristock.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.udteam.miristock.entity.StockDataEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.udteam.miristock.entity.QStockDataEntity.stockDataEntity;


@Repository
@RequiredArgsConstructor
public class StockDataCustomRepositoryImpl implements StockDataCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private BooleanExpression isSearchEndDateNull(Integer searchStartDate, Integer searchEndDate){
        if(searchEndDate == null) {
            return stockDataEntity.stockDataDate.eq(searchStartDate);
        } else {
            return stockDataEntity.stockDataDate.between(searchStartDate, searchEndDate);
        }
    }

    @Override
    public List<StockDataEntity> findStockData(Integer searchStartDate, Integer searchEndDate, String searchStockCode) {

        JPAQuery<StockDataEntity> query = jpaQueryFactory
                    .selectFrom(stockDataEntity)
                    .where(stockDataEntity.stockCode.eq(searchStockCode)
                            .and(isSearchEndDateNull(searchStartDate, searchEndDate))
                    ).fetchAll();
        return query.fetch();

    }
}
