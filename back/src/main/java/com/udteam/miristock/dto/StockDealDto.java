package com.udteam.miristock.dto;

import com.udteam.miristock.entity.Deal;
import com.udteam.miristock.entity.StockDealEntity;
import lombok.*;

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
    private Integer stockDealClosingPrice;
    private Integer stockDealAmount;
    private Deal stockDealType;

    @Builder
    public StockDealDto(StockDealEntity entity) {
        this.stockDealNo = entity.getStockDealNo();
        this.stockCode = entity.getStockCode();
        this.stockName = entity.getStockName();
        this.memberNo = entity.getMemberNo();
        this.stockDealDate = entity.getStockDealDate();
        this.stockDealClosingPrice = entity.getStockDealClosingPrice();
        this.stockDealAmount = entity.getStockDealAmount();
        this.stockDealType = entity.getStockDealType();
    }

    public StockDealEntity toEntity() {
        return StockDealEntity.builder()
                .stockDealNo(stockDealNo)
                .stockCode(stockCode)
                .stockName(stockName)
                .memberNo(memberNo)
                .stockDealDate(stockDealDate)
                .stockDealClosingPrice(stockDealClosingPrice)
                .stockDealAmount(stockDealAmount)
                .stockDealType(stockDealType)
                .build();
    }
}
