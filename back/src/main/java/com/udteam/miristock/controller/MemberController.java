package com.udteam.miristock.controller;

import com.nimbusds.oauth2.sdk.ErrorResponse;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.util.HeaderUtil;
import com.udteam.miristock.util.ReturnMessage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberservice;

    @GetMapping
    @ApiOperation(value ="모든 유저 호출 (TEST 용)")
    public ResponseEntity<List<MemberDto>> selectAllMember(){
        return ResponseEntity.ok().body(memberservice.selectAllMember());
    }

    @DeleteMapping
    @ApiOperation(value = "해당 유저 회원 탈퇴 처리")
    public ResponseEntity<String> deleteMember(@RequestHeader String Authorization){
        log.info("회원 삭제 요청됨");
        String userDetails = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = HeaderUtil.getAccessTokenString(Authorization);
        log.info("유저 디테일 = {}",userDetails);
        if (memberservice.deleteMember(token) != 0){
            // 회원삭제 성공
            return ResponseEntity.status(HttpStatus.OK).body("Delete Member Success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete Member Fail");
    }

    @PutMapping("/nickname")
    @ApiOperation(value = " 유저 닉네임 설정")
    public ResponseEntity<MemberDto> updateMember(@RequestHeader String Authorization, @RequestParam String nickname){
        String userDetails = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = HeaderUtil.getAccessTokenString(Authorization);
        log.info("유저 디테일 = {}",userDetails);
        if(memberservice.selectOneMember(token)== null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok().body(memberservice.updateMember(token,nickname));
    }
}
