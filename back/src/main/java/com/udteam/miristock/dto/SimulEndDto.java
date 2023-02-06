package com.udteam.miristock.dto;


import com.udteam.miristock.entity.MemberAssetEntity;
import com.udteam.miristock.entity.MemberStockEntity;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SimulEndDto {
    MemberAssetEntity memberAsset;
    MemberStockEntity lowMemberStock;
    MemberStockEntity highMemberStock;
}
