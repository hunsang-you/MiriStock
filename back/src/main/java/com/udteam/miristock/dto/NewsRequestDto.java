package com.udteam.miristock.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
public class NewsRequestDto {
    private String searchKeyword;
    private Integer startDate;
    private Integer endDate;

    @Builder
    public NewsRequestDto(String searchKeyword, Integer startDate, Integer endDate) {
        this.searchKeyword = searchKeyword;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Builder
    public NewsRequestDto(String searchKeyword, Integer startDate) {
        this.searchKeyword = searchKeyword;
        this.startDate = startDate;
    }
}
