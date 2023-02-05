package com.udteam.miristock.repository;

import com.udteam.miristock.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, String> {

    // 코드로 검색
    List<StockEntity> findByStockCodeStartingWithOrderByStockCodeAsc(String stockCode);

    // 종목명으로 검색
    List<StockEntity> findByStockNameStartingWithOrderByStockCodeAsc(String stockName);

}
