package com.udteam.miristock.service;

import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.repository.MemberRepository;
import com.udteam.miristock.service.auth.TokenService;
import com.udteam.miristock.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public int deleteMember(String token){
        String email = tokenservice.getUid(token);
        return memberrepository.deleteByMemberEmail(email);
    }
}
