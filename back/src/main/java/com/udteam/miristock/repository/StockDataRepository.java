package com.udteam.miristock.repository;

import com.udteam.miristock.entity.StockDataEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface StockDataRepository extends JpaRepository<StockDataEntity, Long> {

    // 등락률 가장 낮은 순서대로 5개 출력
    public List<StockDataEntity> findTop5ByStockDataDateOrderByStockDataFlucauationRateAsc(int stockDataDate);

    // 등락률 가장 높은 순서대로 5개 출력
    public List<StockDataEntity> findTop5ByStockDataDateOrderByStockDataFlucauationRateDesc(int stockDataDate);


}
