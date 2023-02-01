package com.udteam.miristock.dto;

import com.udteam.miristock.entity.StockDataEntity;
import lombok.*;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StockDataRequestDto {

//    private Long stockDataNo;
    private String stockCode;
    private String stockName;
    private int stockDataDate;
    private int stockDataClosingPrice;
    private Long stockDataAmount;
    private Long stockDataPriceIncreasement;
    private Long stockDataFlucauationRate;

    public StockDataEntity toEntity(){
        return StockDataEntity.builder()
                .stockCode(stockCode)
                .stockName(stockName)
                .stockDataDate(stockDataDate)
                .stockDataClosingPrice(stockDataClosingPrice)
                .stockDataAmount(stockDataAmount)
                .stockDataPriceIncreasement(stockDataPriceIncreasement)
                .stockDataFlucauationRate(stockDataFlucauationRate)
                .build();
    }

}
