package com.udteam.miristock.dto;

import com.udteam.miristock.entity.MemberEntity;
import lombok.*;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private Integer memberNo;
    private String memberEmail;
    private String memberNickname;
    private Long memberTotalasset;
    private Integer memberCurrentTime;

    public static MemberDto of(MemberEntity member){
        return MemberDto.builder()
                .memberNo(member.getMemberNo())
                .memberEmail(member.getMemberEmail())
                .memberNickname(member.getMemberNickname())
                .memberTotalasset(member.getMemberTotalasset())
                .memberCurrentTime(member.getMemberCurrentTime())
                .build();
    }
}
