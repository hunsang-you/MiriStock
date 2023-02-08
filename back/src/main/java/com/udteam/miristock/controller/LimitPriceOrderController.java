package com.udteam.miristock.controller;

import com.udteam.miristock.util.ErrorMessage;
import com.udteam.miristock.dto.LimitPriceOrderDto;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.entity.Deal;
import com.udteam.miristock.service.LimitPriceOrderService;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.util.HeaderUtil;
import com.udteam.miristock.util.ReturnMessage;
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
    public ResponseEntity<List<LimitPriceOrderDto>> findAll(@RequestHeader String Authorization, @RequestParam(required = false) Deal limitPriceOrderType) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(limitPriceOrderService.findAll(m.getMemberNo(), limitPriceOrderType));
        }
    }

    @PostMapping
    public ResponseEntity<LimitPriceOrderDto> save(@RequestHeader String Authorization, @RequestBody LimitPriceOrderDto limitPriceOrderDto) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        log.info("매수, 매도 요청 등록됨 limitPriceOrderDto {} , ", limitPriceOrderDto);
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            limitPriceOrderDto.setMemberNo(m.getMemberNo());
            return ResponseEntity.status(HttpStatus.OK).body(limitPriceOrderService.save(limitPriceOrderDto));
        }
    }

    @PutMapping
    public ResponseEntity<LimitPriceOrderDto> update(@RequestHeader String Authorization, @RequestBody LimitPriceOrderDto limitPriceOrderDto) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            limitPriceOrderDto.setMemberNo(m.getMemberNo());
            return ResponseEntity.status(HttpStatus.OK).body(limitPriceOrderService.save(limitPriceOrderDto));
        }
    }

    @DeleteMapping("/{limitPriceOrderNo}")
    public ResponseEntity<String> delete(@RequestHeader String Authorization, @PathVariable Integer limitPriceOrderNo) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            limitPriceOrderService.delete(m.getMemberNo(), limitPriceOrderNo);
            return ResponseEntity.status(HttpStatus.OK).body(ReturnMessage.DELETE_SUCCESS);
        }
    }

}
