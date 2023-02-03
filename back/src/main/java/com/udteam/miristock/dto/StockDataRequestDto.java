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
    private Integer stockDataDate;
    private Integer stockDataClosingPrice;
    private Long stockDataAmount;
    private Integer stockDataPriceIncreasement;
    private Float stockDataFlucauationRate;

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
