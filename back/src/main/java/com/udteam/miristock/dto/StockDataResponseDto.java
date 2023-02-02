package com.udteam.miristock.dto;

import com.udteam.miristock.entity.StockDataEntity;
import lombok.*;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StockDataResponseDto {

//    private Long stockDataNo;
    private String stockCode;
    private String stockName;
    private Integer stockDataDate;
    private Integer stockDataClosingPrice;
    private Long stockDataAmount;
    private Integer stockDataPriceIncreasement;
    private Float stockDataFlucauationRate;

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
