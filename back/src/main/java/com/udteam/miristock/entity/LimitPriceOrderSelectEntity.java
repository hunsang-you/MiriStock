package com.udteam.miristock.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LimitPriceOrderSelectEntity {

    @Id
    @Column(name = "stockdata_no")
    Integer stockDataNo;
    @Column(name = "stock_code")
    String stockCode;
    @Column(name = "stock_name")
    String stockName;
    @Column(name = "stockdata_closing_price")
    Integer stockDataClosingPrice;
    @Column(name = "stockdata_amount")
    Long stockDataAmount;
    @Column(name = "stockdata_price_increasement")
    Integer stockDataPriceIncreasement;
    @Column(name = "stockdata_flucauationrate")
    Float stockDataFlucauationRate;

    @Column(name = "limitpriceorder_price")
    Integer limitPriceOrderPrice;

    @Column(name = "limitpriceorder_amount")
    Long limitPriceOrderAmount;

    @Column(name = "limitpriceorder_type")
    Deal limitPriceOrderType;

}
