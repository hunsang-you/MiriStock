package com.udteam.miristock.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "search")
public class SearchRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long searchNo;
    private Long memberNo;
    private String stockCode;
    private String stockName;

    @Builder
    public SearchRecord(Long searchNo, Long memberNo, String stockCode, String stockName) {
        this.searchNo = searchNo;
        this.memberNo = memberNo;
        this.stockCode = stockCode;
        this.stockName = stockName;
    }
}
