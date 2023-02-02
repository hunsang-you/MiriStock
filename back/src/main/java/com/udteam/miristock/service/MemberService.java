package com.udteam.miristock.service;

import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.entity.MemberEntity;
import com.udteam.miristock.repository.MemberRepository;
import com.udteam.miristock.service.auth.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberrepository;
    private final TokenService tokenservice;

    public List<MemberDto> selectAllMember(){
        return memberrepository.findAll()
                .stream()
                .map(MemberDto::of)
                .collect(Collectors.toList());
    }

    public Integer deleteMember(String token){
        String email = tokenservice.getUid(token);
        log.info("이메일 출력={}",email);
        return memberrepository.deleteByMemberEmail(email);
    }

    @Modifying
    public MemberDto updateMember(String token,String nickname){
        String email = tokenservice.getUid(token);
        MemberEntity member = memberrepository.findByMemberEmail(email);
        member.setMemberNickname(nickname);
//        MemberEntity updatemember= MemberEntity.builder()
//                .memberNo(member.getMemberNo())
//                .memberEmail(member.getMemberEmail())
//                .memberNickname(nickname)
//                .memberProvider(member.getMemberProvider())
//                .memberTotalasset(member.getMemberTotalasset())
//                .memberCurrentTime(member.getMemberCurrentTime())
//                .role(member.getRole())
//                .build();
        return MemberDto.of(memberrepository.saveAndFlush(member));
    }

    public MemberDto selectOneMember(String token){
        String email = tokenservice.getUid(token);
        return MemberDto.of(memberrepository.findByMemberEmail(email));
    }

}
