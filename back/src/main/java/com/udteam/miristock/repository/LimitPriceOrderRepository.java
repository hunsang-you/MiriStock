package com.udteam.miristock.repository;

import com.udteam.miristock.entity.Deal;
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
        " WHERE s.stockDataDate=:stockDataDate AND l.memberNo=:memberNo AND l.limitPriceOrderType=:limitPriceOrderType")
    List<Object[]> compareLimitPriceOrderWithTodayStockData(
            @Param("memberNo") Integer memberNo,
            @Param("stockDataDate") Integer stockDataDate,
            @Param("limitPriceOrderType") Deal limitPriceOrderType);

    int deleteAllByMemberNoAndLimitPriceOrderNo(Integer memberNo, Integer limitPriceOrderNo);

    List<LimitPriceOrderEntity> findAllByMemberNo(Integer memberNo);

    // 특정 거래 타입 거래예정 내역 삭제
    void deleteAllByMemberNoAndLimitPriceOrderType(Integer memberNo, Deal type);

    void deleteByMemberNo(Integer memberNo);

    void deleteAllByMemberNo(Integer memberNo);


}
