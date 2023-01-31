package com.udteam.miristock.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name ="stock")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StockEntity {

    @Id
    @Column(name="stock_code")
    private String stockCode;

    @Column(name="stock_name")
    private String stockName;
}
