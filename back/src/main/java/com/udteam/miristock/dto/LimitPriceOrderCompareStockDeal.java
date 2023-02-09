package com.udteam.miristock.dto;

import com.udteam.miristock.entity.Deal;
import com.udteam.miristock.entity.LimitPriceOrderEntity;
import com.udteam.miristock.entity.StockDataEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LimitPriceOrderCompareStockDeal {
    private Integer stockDataNo;
    private String stockCode;
    private String stockName;
    private Integer stockDataDate;
    private Long stockDataClosingPrice;
    private Long stockDataAmount;
    private Long stockDataPriceIncreasement;
    private Float stockDataFlucauationRate;
    private Integer memberNo;
    private Integer limitPriceOrderNo;
    private Long limitPriceOrderPrice;
    private Long limitPriceOrderAmount;
    private Deal limitPriceOrderType;

    @Builder
    public LimitPriceOrderCompareStockDeal (StockDataEntity stockDataEntity, LimitPriceOrderEntity limitPriceOrderEntity){
        this.stockDataNo = stockDataEntity.getStockDataNo();
        this.stockCode = stockDataEntity.getStockCode();
        this.stockName = stockDataEntity.getStockName();
        this.stockDataDate = stockDataEntity.getStockDataDate();
        this.stockDataClosingPrice = stockDataEntity.getStockDataClosingPrice();
        this.stockDataAmount = stockDataEntity.getStockDataAmount();
        this.stockDataPriceIncreasement = stockDataEntity.getStockDataPriceIncreasement();
        this.stockDataFlucauationRate = stockDataEntity.getStockDataFlucauationRate();
        this.memberNo = limitPriceOrderEntity.getMemberNo();
        this.limitPriceOrderNo = limitPriceOrderEntity.getLimitPriceOrderNo();
        this.limitPriceOrderPrice = limitPriceOrderEntity.getLimitPriceOrderPrice();
        this.limitPriceOrderAmount = limitPriceOrderEntity.getLimitPriceOrderAmount();
        this.limitPriceOrderType = limitPriceOrderEntity.getLimitPriceOrderType();
    }

}
