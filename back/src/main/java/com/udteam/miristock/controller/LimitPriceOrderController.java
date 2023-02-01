package com.udteam.miristock.controller;

import com.udteam.miristock.config.ErrorMessage;
import com.udteam.miristock.dto.LimitPriceOrderDto;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.service.LimitPriceOrderService;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/limitpriceorder")
@RequiredArgsConstructor
public class LimitPriceOrderController {

    private final LimitPriceOrderService limitPriceOrderService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<LimitPriceOrderDto>> findAll(@RequestHeader String Authorization) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            // 엑세스 토큰 재 발급 프로세스 필요
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } else {
            return ResponseEntity.ok()
                    .body(limitPriceOrderService.findAll((int) m.getMemberNo()));
        }
    }

    @PostMapping
    public ResponseEntity<Integer> save(@RequestHeader String Authorization, @RequestBody LimitPriceOrderDto limitPriceOrderDto) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } else {
            limitPriceOrderDto.setMemberNo((int) m.getMemberNo());
            return ResponseEntity.ok()
                    .body(limitPriceOrderService.save(limitPriceOrderDto));
        }
    }

    @PutMapping
    public ResponseEntity<Integer> update(@RequestHeader String Authorization, @RequestBody LimitPriceOrderDto limitPriceOrderDto) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } else {
            limitPriceOrderDto.setMemberNo((int) m.getMemberNo());
            return ResponseEntity.ok()
                    .body(limitPriceOrderService.save(limitPriceOrderDto));
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestHeader String Authorization, @RequestHeader Integer limitPriceOrderNo) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } else {
            limitPriceOrderService.delete(limitPriceOrderNo);
            return ResponseEntity.ok()
                    .body(null);
        }
    }

}
