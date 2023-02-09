package com.udteam.miristock.dto;

import com.udteam.miristock.entity.MemberStockEntity;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberStockDto {

    private Integer memberStockNo;
    private String stockCode;
    private String stockName;
    private Integer memberNo;
    private Long memberStockAmount;
    private Long memberStockAvgPrice;
    private Long memberStockAccPurchasePrice;
    private Long memberStockAccSellPrice;
    private Float memberStockAccEarnRate;

    @Builder
    public MemberStockDto(MemberStockEntity entity) {
        this.memberStockNo = entity.getMemberStockNo();
        this.stockCode = entity.getStockCode();
        this.stockName = entity.getStockName();
        this.memberNo = entity.getMemberNo();
        this.memberStockAmount = entity.getMemberStockAmount();
        this.memberStockAvgPrice = entity.getMemberStockAvgPrice();
        this.memberStockAccPurchasePrice = entity.getMemberStockAccPurchasePrice();
        this.memberStockAccSellPrice = entity.getMemberStockAccSellPrice();
        this.memberStockAccEarnRate = entity.getMemberStockAccEarnRate();
    }

    public MemberStockEntity toEntity() {
        return MemberStockEntity.builder()
                .memberStockNo(memberStockNo)
                .stockCode(stockCode)
                .stockName(stockName)
                .memberNo(memberNo)
                .memberStockAmount(memberStockAmount)
                .memberStockAvgPrice(memberStockAvgPrice)
                .memberStockAccPurchasePrice(memberStockAccPurchasePrice)
                .memberStockAccSellPrice(memberStockAccSellPrice)
                .memberStockAccEarnRate(memberStockAccEarnRate)
                .build();
    }
}
