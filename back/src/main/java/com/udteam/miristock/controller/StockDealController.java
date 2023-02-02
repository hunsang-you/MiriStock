package com.udteam.miristock.controller;

import com.udteam.miristock.config.ErrorMessage;
import com.udteam.miristock.dto.LimitPriceOrderDto;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.dto.StockDealDto;
import com.udteam.miristock.entity.Deal;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.service.StockDealService;
import com.udteam.miristock.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/stockdeal")
@RequiredArgsConstructor
public class StockDealController {

    private final StockDealService stockDealService;

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<StockDealDto>> findAll(@RequestHeader String Authorization, @RequestParam(required = false) Deal stockdealtype) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.ok().body(stockDealService.findAllByMemberNoAndStockDealType((int) m.getMemberNo(), stockdealtype));
        }
    }

    @PostMapping
    public ResponseEntity<Integer> save(@RequestHeader String Authorization, @RequestBody StockDealDto stockDealDto) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            stockDealDto.setMemberNo((int) m.getMemberNo());
            return ResponseEntity.ok().body(stockDealService.save(stockDealDto));
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestHeader String Authorization) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            log.info("delete log1");
            stockDealService.delete((int) m.getMemberNo());
            log.info("delete log2");
            return ResponseEntity.ok().body(null);
        }
    }
}
