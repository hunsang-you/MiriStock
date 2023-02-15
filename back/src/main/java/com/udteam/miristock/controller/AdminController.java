package com.udteam.miristock.controller;

import com.udteam.miristock.dto.MemberAdminDto;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.entity.Role;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.util.ErrorMessage;
import com.udteam.miristock.util.HeaderUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberservice;

    @GetMapping("/member/list")
    @Operation(summary = "회원 목록 출력", description = "서비스에 가입된 회원 목록을 출력한다.", tags = { "Member" })
    public ResponseEntity<List<MemberDto>> selectAllMember(@RequestHeader String Authorization){
        MemberAdminDto m = memberservice.selectOneMemberAllInfo(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else if(m.getROLE().equals(Role.ADMIN)) {
            return ResponseEntity.ok().body(memberservice.selectAllMember());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

}
