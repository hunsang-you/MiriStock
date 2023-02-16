package com.udteam.miristock.dto;

import com.udteam.miristock.entity.MemberEntity;
import com.udteam.miristock.entity.Role;
import lombok.*;

@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberAdminDto {
    private Integer memberNo;
    private String memberEmail;
    private String memberNickname;
    private String memberProvider;
    private Role ROLE;

    public static MemberAdminDto of(MemberEntity member){
        return MemberAdminDto.builder()
                .memberNo(member.getMemberNo())
                .memberEmail(member.getMemberEmail())
                .memberNickname(member.getMemberNickname())
                .memberProvider(member.getMemberProvider())
                .ROLE(member.getRole())
                .build();
    }
}
