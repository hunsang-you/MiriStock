package com.udteam.miristock.dto;

import com.udteam.miristock.entity.FinancialstatementEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FinancialstatementDto {

    private String stockCode;
    private Long newIncome;
    private Integer year;
    private Long salesRevenue;
    private Long operatingProfit;

    public FinancialstatementDto(FinancialstatementEntity entity) {
        this.stockCode = entity.getStockCode();
        this.newIncome = entity.getNewIncome();
        this.year = entity.getYear();
        this.salesRevenue = entity.getSalesRevenue();
        this.operatingProfit = entity.getOperatingProfit();
    }

    public FinancialstatementEntity toEntity() {
        return FinancialstatementEntity.builder()
                .stockCode(stockCode)
                .newIncome(newIncome)
                .year(year)
                .salesRevenue(salesRevenue)
                .operatingProfit(operatingProfit)
                .build();
    }
}
