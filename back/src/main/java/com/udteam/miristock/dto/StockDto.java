package com.udteam.miristock.dto;

import lombok.*;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StockDto {
    private String stock_code;
    private String stock_name;

}
