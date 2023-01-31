package com.udteam.miristock.dto;

import com.udteam.miristock.entity.StockDataEntity;
import lombok.Getter;

@Getter
public class StockDataResponseDto {

//    private Long stockDataNo;
    private String stockCode;
    private String stockName;
    private int stockDataDate;
    private int stockDataClosingPrice;
    private Long stockDataAmount;
    private Long stockDataPriceIncreasement;
    private Long stockDataFlucauationRate;

    public StockDataResponseDto(StockDataEntity entity){
//        this.stockDataNo = entity.getStockDataNo();
        this.stockCode = entity.getStockCode();
        this.stockName = entity.getStockName();
        this.stockDataDate = entity.getStockDataDate();
        this.stockDataClosingPrice = entity.getStockDataClosingPrice();
        this.stockDataAmount = entity.getStockDataAmount();
        this.stockDataPriceIncreasement = entity.getStockDataPriceIncreasement();
        this.stockDataFlucauationRate = entity.getStockDataFlucauationRate();
    }


}
