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
    private Long memberStockAvgPriceSum;
    private Long memberStockCurPrice;
    private Long memberStockCurPriceSum;
    private Float memberStockAccEarnRate;
    private Long memberStockAccEarnPrice;

    @Builder
    public MemberStockDto(MemberStockEntity entity) {
        this.memberStockNo = entity.getMemberStockNo();
        this.stockCode = entity.getStockCode();
        this.stockName = entity.getStockName();
        this.memberNo = entity.getMemberNo();
        this.memberStockAmount = entity.getMemberStockAmount();
        this.memberStockAvgPrice = entity.getMemberStockAvgPrice();
        this.memberStockAvgPriceSum = entity.getMemberStockAvgPriceSum();
        this.memberStockCurPrice = entity.getMemberStockCurPrice();
        this.memberStockCurPriceSum = entity.getMemberStockCurPriceSum();
        this.memberStockAccEarnRate = entity.getMemberStockAccEarnRate();
        this.memberStockAccEarnPrice = entity.getMemberStockAccEarnPrice();
    }

    public MemberStockEntity toEntity() {
        return MemberStockEntity.builder()
                .memberStockNo(memberStockNo)
                .stockCode(stockCode)
                .stockName(stockName)
                .memberNo(memberNo)
                .memberStockAmount(memberStockAmount)
                .memberStockAvgPrice(memberStockAvgPrice)
                .memberStockAvgPriceSum(memberStockAvgPriceSum)
                .memberStockCurPrice(memberStockCurPrice)
                .memberStockCurPriceSum(memberStockCurPriceSum)
                .memberStockAccEarnRate(memberStockAccEarnRate)
                .memberStockAccEarnPrice(memberStockAccEarnPrice)
                .build();
    }
}
