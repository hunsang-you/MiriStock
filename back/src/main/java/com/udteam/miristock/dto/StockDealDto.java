package com.udteam.miristock.dto;

import com.udteam.miristock.entity.Deal;
import com.udteam.miristock.entity.StockDealEntity;
import lombok.*;

import javax.persistence.Column;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StockDealDto {
    private Integer stockDealNo;
    private String stockCode;
    private String stockName;
    private Integer memberNo;
    private Integer stockDealDate;
    private Long stockDealOrderClosingPrice;
    private Long stockDealAvgClosingPrice;
    private Long stockDealAmount;
    private Deal stockDealType;
    private Float stockDealEarnRate;
    private Long stockDealEarnPrice;

    @Builder
    public StockDealDto(StockDealEntity entity) {
        this.stockDealNo = entity.getStockDealNo();
        this.stockCode = entity.getStockCode();
        this.stockName = entity.getStockName();
        this.memberNo = entity.getMemberNo();
        this.stockDealDate = entity.getStockDealDate();
        this.stockDealOrderClosingPrice = entity.getStockDealOrderClosingPrice();
        this.stockDealAvgClosingPrice = entity.getStockDealAvgClosingPrice();
        this.stockDealAmount = entity.getStockDealAmount();
        this.stockDealType = entity.getStockDealType();
        this.stockDealEarnRate = entity.getStockDealEarnRate();
        this.stockDealEarnPrice = entity.getStockDealEarnPrice();
    }

    public StockDealEntity toEntity() {
        return StockDealEntity.builder()
                .stockDealNo(stockDealNo)
                .stockCode(stockCode)
                .stockName(stockName)
                .memberNo(memberNo)
                .stockDealDate(stockDealDate)
                .stockDealOrderClosingPrice(stockDealOrderClosingPrice)
                .stockDealAvgClosingPrice(stockDealAvgClosingPrice)
                .stockDealAmount(stockDealAmount)
                .stockDealType(stockDealType)
                .stockDealEarnRate(stockDealEarnRate)
                .stockDealEarnPrice(stockDealEarnPrice)
                .build();
    }
}
