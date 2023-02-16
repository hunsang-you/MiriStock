package com.udteam.miristock.dto;


import com.udteam.miristock.entity.MemberAssetEntity;
import com.udteam.miristock.entity.MemberStockEntity;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SimulEndDto {
    MemberAssetEntity memberAsset;
    List<MemberSimulEndDto> lowMemberStock;
    List<MemberSimulEndDto> highMemberStock;
}
