package com.udteam.miristock.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="financialstatement")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FinancialstatementEntity {
    @Id
    @Column(name = "financialstatement_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer financialstatementNo;
    @Column(name = "stock_code")
    private String stockCode;
    @Column(name = "net_income")
    private Long newIncome;
    @Column(name = "year")
    private Integer year;
    @Column(name = "sales_revenue")
    private Long salesRevenue;
    @Column(name = "operating_profit")
    private Long operatingProfit;

}
