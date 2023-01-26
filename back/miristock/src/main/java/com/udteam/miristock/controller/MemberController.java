package com.udteam.miristock.controller;

import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberservice;

    @GetMapping
    public ResponseEntity<List<MemberDto>> selectAllMember(){
        return ResponseEntity.ok().body(memberservice.selectAllMember());
    }
}
