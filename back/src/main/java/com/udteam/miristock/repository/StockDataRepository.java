package com.udteam.miristock.repository;

import com.udteam.miristock.dto.StockDataInfoMapping;
import com.udteam.miristock.dto.StockDataSearchResponseMapping;
import com.udteam.miristock.entity.StockDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockDataRepository extends JpaRepository<StockDataEntity, Integer> {

    // 거래량 많은 5개 불러오기
//    List<StockDataEntity> findTopByStockCodeOrderByStockDataAmountDesc(Integer stockDataDate);
    List<StockDataEntity> findTop5ByStockDataDateOrderByStockDataAmountDesc(Integer stockDataDate);

    // 등락률 가장 낮은 순서대로 5개 출력
    List<StockDataEntity> findTop5ByStockDataDateOrderByStockDataFlucauationRateAsc(Integer stockDataDate);

    // 등락률 가장 높은 순서대로 5개 출력
    List<StockDataEntity> findTop5ByStockDataDateOrderByStockDataFlucauationRateDesc(Integer stockDataDate);

    // 해당 날짜에 데이터가 있는지 확인
    StockDataInfoMapping findTop1ByStockDataDate(Integer stockDataDate);

    // 해당 날짜 데이터 있는 주식 코드 검색
    List<StockDataSearchResponseMapping> findAllByStockCodeStartingWithAndStockDataDateOrderByStockCodeAsc(String StockCode, Integer StockDataDate);

    // 해당 날짜 데이터 있는 주식 명칭 검색
    List<StockDataSearchResponseMapping> findAllByStockNameStartingWithAndStockDataDateOrderByStockCodeAsc(String stockName, Integer StockDataDate);
}
