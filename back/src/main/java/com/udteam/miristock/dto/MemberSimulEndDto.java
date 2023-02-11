package com.udteam.miristock.dto;

import com.udteam.miristock.entity.MemberStockEntity;
import lombok.Builder;
import lombok.Data;

@Data
public class MemberSimulEndDto {
    private Integer memberStockNo;
    private String stockCode;
    private String stockName;
    private Integer memberNo;
    private Long memberStockAccPurchasePrice;
    private Long memberStockAccSellPrice;
    private Long memberStockEarnPrice;
    private Float memberStockEarnRate;

    @Builder
    public  MemberSimulEndDto(MemberStockEntity entity) {
        this.memberStockNo = entity.getMemberStockNo();
        this.stockCode = entity.getStockCode();
        this.stockName = entity.getStockName();
        this.memberNo = entity.getMemberNo();
        this.memberStockAccPurchasePrice = entity.getMemberStockAccPurchasePrice();
        this.memberStockAccSellPrice = entity.getMemberStockAccSellPrice();
        this.memberStockEarnPrice = entity.getMemberStockAccSellPrice() - entity.getMemberStockAccPurchasePrice();
        this.memberStockEarnRate = entity.getMemberStockAccEarnRate();
    }
}
