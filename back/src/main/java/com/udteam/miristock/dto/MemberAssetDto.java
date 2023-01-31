package com.udteam.miristock.dto;

import com.udteam.miristock.entity.MemberAssetEntity;
import lombok.*;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberAssetDto {
    private int memberassetNo;
    private int memberNo;
    private long memberassetTotalAsset;
    private long memberassetAvailableAsset;
    private long memberassetStockAsset;

    public static MemberAssetDto of(MemberAssetEntity m){
        return MemberAssetDto.builder()
                .memberassetNo(m.getMemberassetNo())
                .memberNo(m.getMember().getMemberNo())
                .memberassetTotalAsset(m.getMemberassetTotalAsset())
                .memberassetAvailableAsset(m.getMemberassetAvailableAsset())
                .memberassetStockAsset(m.getMemberassetStockAsset())
                .build();
    }
}
