package com.udteam.miristock.dto;

import com.udteam.miristock.entity.MemberAssetEntity;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberAssetDto {
    private Integer memberassetNo;
    private Integer memberNo;
    private Long memberassetTotalAsset;
    private Long memberassetAvailableAsset;
    private Long memberassetStockAsset;
    private Integer memberassetCurrentTime;
    private Long memberassetLastTotalAsset;


    public static MemberAssetDto of(MemberAssetEntity m){
        return MemberAssetDto.builder()
                .memberassetNo(m.getMemberassetNo())
                .memberNo(m.getMember().getMemberNo())
                .memberassetTotalAsset(m.getMemberassetTotalAsset())
                .memberassetAvailableAsset(m.getMemberassetAvailableAsset())
                .memberassetStockAsset(m.getMemberassetStockAsset())
                .memberassetCurrentTime(m.getMemberassetCurrentTime())
                .memberassetLastTotalAsset(m.getMemberassetLastTotalAsset())
                .build();
    }
}
