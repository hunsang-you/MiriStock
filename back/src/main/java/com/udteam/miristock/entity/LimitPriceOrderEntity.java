package com.udteam.miristock.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name="limitpriceorder")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LimitPriceOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "limitpriceorder_no")
    private Integer limitPriceOrderNo;

    @Column(name = "stock_code")
    private String stockCode;

    @Column(name = "stock_name")
    private String stockName;

    @Column(name = "member_no")
    private Integer memberNo;

    @Column(name = "limitpriceorder_price")
    private Long limitPriceOrderPrice;

    @Column(name = "limitpriceorder_amount")
    private Long limitPriceOrderAmount;

    @Column(name = "limitpriceorder_type")
    @Enumerated(EnumType.STRING)
    private Deal limitPriceOrderType;

}
