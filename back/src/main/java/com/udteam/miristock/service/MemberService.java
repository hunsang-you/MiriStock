package com.udteam.miristock.service;

import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberrepository;

    public List<MemberDto> selectAllMember(){
        return memberrepository.findAll()
                .stream()
                .map(MemberDto::of)
                .collect(Collectors.toList());
    }
}
