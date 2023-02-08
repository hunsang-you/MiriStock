package com.udteam.miristock.controller;

import com.udteam.miristock.util.ErrorMessage;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.dto.MemberStockDto;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.service.MemberStockService;
import com.udteam.miristock.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/asset/memberstock")
@RequiredArgsConstructor
public class MemberStockController {

    private final MemberStockService memberStockService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<MemberStockDto>> findAll(@RequestHeader String Authorization) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.ok().body(memberStockService.findAll(m.getMemberNo()));
        }
    }

    // create test
    @PostMapping
    public ResponseEntity<MemberStockDto> save(@RequestHeader String Authorization, @RequestBody MemberStockDto memberStockDto) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            memberStockDto.setMemberNo(m.getMemberNo());
            return ResponseEntity.ok().body(memberStockService.save(memberStockDto));
        }
    }

    // update test
    @PutMapping
    public ResponseEntity<MemberStockDto> update(@RequestHeader String Authorization, @RequestBody MemberStockDto memberStockDto) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            memberStockDto.setMemberNo(m.getMemberNo());
            return ResponseEntity.ok().body(memberStockService.save(memberStockDto));
        }
    }

    // delete test
    @DeleteMapping
    public ResponseEntity<MemberStockDto> delete(@RequestHeader String Authorization, @RequestParam String stockCode) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            memberStockService.delete(m.getMemberNo(), stockCode);
            return ResponseEntity.ok().body(null);
        }
    }

}
