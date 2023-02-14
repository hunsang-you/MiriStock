package com.udteam.miristock.service;

import com.udteam.miristock.dto.MemberAdminDto;
import com.udteam.miristock.dto.MemberAssetDto;
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
        String email = tokenservice.getEmail(token);
        log.info("이메일 출력={}",email);
        return memberrepository.deleteByMemberEmail(email);
    }

    @Modifying
    public MemberDto updateMember(String token,String nickname){
        String email = tokenservice.getEmail(token);
        MemberEntity member = memberrepository.findByMemberEmail(email);
        member.setMemberNickname(nickname);
        return MemberDto.of(memberrepository.saveAndFlush(member));
    }

    @Modifying
    public MemberDto updateNickName(MemberDto memberDto) {
        MemberEntity getMemberEntity = memberrepository.findByMemberEmail(memberDto.getMemberEmail());
        getMemberEntity.setMemberNickname(memberDto.getMemberNickname());
        log.info("getMemberEntity : {}", getMemberEntity);
        return MemberDto.of(memberrepository.saveAndFlush(getMemberEntity));
    }

    public MemberDto selectOneMember(String token){
        String email = tokenservice.getEmail(token);
        return MemberDto.of(memberrepository.findByMemberEmail(email));
    }

    public MemberAdminDto selectOneMemberAllInfo(String token){
        String email = tokenservice.getEmail(token);
        return MemberAdminDto.of(memberrepository.findByMemberEmail(email));
    }

    public MemberDto selectOnMemberByEmail (MemberDto memberDto){
        String email = memberDto.getMemberEmail();
        return MemberDto.of(memberrepository.findByMemberEmail(email));
    }

}
