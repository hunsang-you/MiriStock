package com.udteam.miristock.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="memberstock")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberStockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="memberstock_no")
    private Integer memberStockNo;

    @Column(name="stock_code")
    private String stockCode;

    @Column(name="stock_name")
    private String stockName;

    @Column(name = "member_no")
    private Integer memberNo;

    @Column(name = "memberstock_amount")
    private Long memberStockAmount;

    @Column(name = "memberstock_avgprice")
    private Long memberStockAvgPrice;

    @Column(name = "memberstock_avgpricesum")
    private Long memberStockAvgPriceSum;

    @Column(name = "memberstock_curprice")
    private Long memberStockCurPrice;

    @Column(name = "memberstock_curpricesum")
    private Long memberStockCurPriceSum;

    @Column(name = "memberstock_earnrate")
    private Float memberStockEarnRate;

    @Column(name = "memberstock_earnprice")
    private Long memberStockEarnPrice;

}
