package com.udteam.miristock.entity;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "stockdata")

public class StockDataEntity {

    @Id
    @Column(name = "stockdata_no")
    private Long stockDataNo;

    @Column(name = "stock_code")
    private String stockCode;

    @Column(name = "stock_name")
    private String stockName;

    @Column(name = "stockdata_date")
    private int stockDataDate;

    @Column(name = "stockdata_closing_price")
    private int stockDataClosingPrice;

    @Column(name = "stockdata_amount")
    private Long stockDataAmount;

    @Column(name = "stockdata_price_increasement")
    private Long stockDataPriceIncreasement;

    @Column(name = "stockdata_flucauation_rate")
    private Long stockDataFlucauationRate;

    @Builder
    public StockDataEntity (Long stockDataNo, String stockCode, String stockName, int stockDataDate, int stockDataClosingPrice,
                            Long stockDataAmount, Long stockDataPriceIncreasement, Long stockDataFlucauationRate) {
        this.stockDataNo = stockDataNo;
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.stockDataDate = stockDataDate;
        this.stockDataClosingPrice = stockDataClosingPrice;
        this.stockDataAmount = stockDataAmount;
        this.stockDataPriceIncreasement = stockDataPriceIncreasement;
        this.stockDataFlucauationRate = stockDataFlucauationRate;
    }

}
