package com.udteam.miristock.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "search")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SearchRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long searchNo;
    private Long memberNo;
    private String stockCode;
    private String stockName;

}
