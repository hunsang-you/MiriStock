package com.udteam.miristock.controller;

import com.udteam.miristock.dto.MemberAssetDto;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.service.MemberAssetService;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.util.HeaderUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
@Tag(name = "MemberAsset", description = "회원 자산")
public class MemberAssetController {

    private final MemberAssetService memberAssetService;
    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "매수/매도 예정 내역 출력", description = "회원의 매수/매도 예정 목록을 출력한다.", tags = { "MemberAsset" })
    public ResponseEntity<MemberAssetDto> selectMemberAsset(@RequestHeader String Authorization) {
        String token= HeaderUtil.getAccessTokenString(Authorization);
        MemberDto m = memberService.selectOneMember(token);
        if (m == null){
            log.info("토큰 기간이 만료되었거나 유저 정보가 존재하지 않습니다");
            // 엑세스 토큰 재 발급 프로세스 필요
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }else {
            return ResponseEntity.ok().body(memberAssetService.selectMemberAsset(m.getMemberNo()));
        }
    }

}
