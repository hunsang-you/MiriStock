package com.udteam.miristock.repository;

import com.udteam.miristock.entity.LimitPriceOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LimitPriceOrderRepository extends JpaRepository<LimitPriceOrderEntity, Integer> {

    @Query(" SELECT s, l " +
        " FROM StockDataEntity AS s " +
        " JOIN LimitPriceOrderEntity AS l " +
        " ON l.stockCode = s.stockCode " +
        " WHERE s.stockDataDate=:stockDataDate AND l.memberNo=:memberNo ")
    List<Object[]> compareLimitPriceOrderWithTodayStockData(@Param("memberNo") Integer memberNo, @Param("stockDataDate") Integer stockDataDate);

    int deleteAllByMemberNoAndLimitPriceOrderNo(Integer memberNo, Integer limitPriceOrderNo);

    List<LimitPriceOrderEntity> findAllByMemberNo(Integer memberNo);

}
