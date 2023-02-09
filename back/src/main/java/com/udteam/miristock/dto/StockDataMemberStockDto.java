package com.udteam.miristock.dto;

import com.udteam.miristock.entity.MemberStockEntity;
import com.udteam.miristock.entity.StockDataEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StockDataMemberStockDto {

    private Integer memberStockNo;
    private String stockCode;
    private String stockName;
    private Integer memberNo;
    private Long memberStockAmount;
    private Long memberStockAvgPrice;
    private Long memberStockAccPurchasePrice;
    private Long memberStockAccSellPrice;
    private Float memberStockAccEarnRate;

    private Integer stockDataDate;
    private Long stockDataClosingPrice;
    private Long stockDataAmount;
    private Long stockDataPriceIncreasement;
    private Float stockDataFlucauationRate;

    @Builder
    public StockDataMemberStockDto(MemberStockEntity memberStockEntity, StockDataEntity stockDataEntity) {
        this.memberStockNo = memberStockEntity.getMemberStockNo();
        this.stockCode = memberStockEntity.getStockCode();
        this.stockName = memberStockEntity.getStockName();
        this.memberNo = memberStockEntity.getMemberNo();
        this.memberStockAmount = memberStockEntity.getMemberStockAmount();
        this.memberStockAvgPrice = memberStockEntity.getMemberStockAvgPrice();
        this.memberStockAccPurchasePrice = memberStockEntity.getMemberStockAccPurchasePrice();
        this.memberStockAccSellPrice = memberStockEntity.getMemberStockAccSellPrice();
        this.memberStockAccEarnRate = memberStockEntity.getMemberStockAccEarnRate();
        this.stockDataDate = stockDataEntity.getStockDataDate();
        this.stockDataClosingPrice = stockDataEntity.getStockDataClosingPrice();
        this.stockDataAmount = stockDataEntity.getStockDataAmount();
        this.stockDataPriceIncreasement = stockDataEntity.getStockDataPriceIncreasement();
        this.stockDataFlucauationRate = stockDataEntity.getStockDataFlucauationRate();
    }


}
