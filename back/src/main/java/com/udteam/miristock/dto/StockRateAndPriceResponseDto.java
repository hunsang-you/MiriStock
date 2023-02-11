package com.udteam.miristock.dto;

import lombok.*;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StockRateAndPriceResponseDto {
    private Float stockDataFlucauationRateSum;
    private Long stockDataPriceIncreasement;

}
