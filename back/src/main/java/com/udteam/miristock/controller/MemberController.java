package com.udteam.miristock.controller;

import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberservice;

    @GetMapping
    @ApiOperation(value ="모든 유저 호출 (TEST 용)")
    public ResponseEntity<List<MemberDto>> selectAllMember(){
        return ResponseEntity.ok().body(memberservice.selectAllMember());
    }

    @DeleteMapping
    @ApiOperation(value = "해당 유저 회원 탈퇴 처리")
    public ResponseEntity<Integer> deleteMember(@RequestBody String token){
        return ResponseEntity.ok().body(memberservice.deleteMember(token));
    }

//    @PutMapping
//    public ResponseEntity<Integer> updateMember(@RequestBody String token){
//
//    }


}
