package com.udteam.miristock.repository;

import com.udteam.miristock.entity.StockDataEntity;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface StockDataCustomRepository {

    // 날짜, 종목코드 넣으면 해당 날짜 주식데이터 나옴
    public List<StockDataEntity> findStockData(Integer searchStartDate, Integer searchEndDate, String searchStockCode);

}
