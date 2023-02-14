package com.udteam.miristock.dto;

import lombok.*;


@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberAssetNickNameResponseDto {
    private Integer memberassetNo;
    private Integer memberNo;
    private Long memberassetTotalAsset;
    private Long memberassetAvailableAsset;
    private Long memberassetStockAsset;
    private Integer memberassetCurrentTime;
    private Long memberassetLastTotalAsset;
    private String memberNickName;

    public static MemberAssetNickNameResponseDto of(MemberAssetDto m, MemberDto md){
        return MemberAssetNickNameResponseDto.builder()
                .memberassetNo(m.getMemberassetNo())
                .memberNo(m.getMemberNo())
                .memberassetTotalAsset(m.getMemberassetTotalAsset())
                .memberassetAvailableAsset(m.getMemberassetAvailableAsset())
                .memberassetStockAsset(m.getMemberassetStockAsset())
                .memberassetCurrentTime(m.getMemberassetCurrentTime())
                .memberassetLastTotalAsset(m.getMemberassetLastTotalAsset())
                .memberNickName(md.getMemberNickname())
                .build();
    }
}
