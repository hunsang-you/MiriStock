package com.udteam.miristock.dto;

import com.udteam.miristock.entity.InterestEntity;
import lombok.*;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InterestDto {
    private int interestNo;
    private String stockCode;
    private int memberNo;

    public static InterestDto of(InterestEntity i){
        return InterestDto.builder()
                .interestNo(i.getInterestNo())
                .stockCode(i.getStock().getStockCode())
                .memberNo(i.getMember().getMemberNo())
                .build();
    }
}
