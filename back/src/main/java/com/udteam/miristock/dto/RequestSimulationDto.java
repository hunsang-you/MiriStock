package com.udteam.miristock.dto;

import com.udteam.miristock.entity.MemberAssetEntity;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestSimulationDto {
    private Integer memberNo;
    private Integer memberassetCurrentTime;

    public static RequestSimulationDto memberEntityToSimulationResponseDto (MemberAssetEntity m){
        return RequestSimulationDto.builder()
                .memberNo(m.getMember().getMemberNo())
                .memberassetCurrentTime(m.getMemberassetCurrentTime())
                .build();
    }
}
