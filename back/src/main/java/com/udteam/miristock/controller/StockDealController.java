package com.udteam.miristock.controller;

import com.udteam.miristock.util.ErrorMessage;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.dto.StockDealDto;
import com.udteam.miristock.entity.Deal;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.service.StockDealService;
import com.udteam.miristock.util.HeaderUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "StockDeal", description = "주식 거래 정보")
public class StockDealController {

    private final StockDealService stockDealService;

    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "회원 주식 거래 정보 출력", description = "회원이 거래한 주식 정보를 출력한다.", tags = { "StockDeal" })
    public ResponseEntity<List<StockDealDto>> findAll(@RequestHeader String Authorization, @RequestParam(required = false) Deal stockdealtype) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        log.info("/stockdata/stockdeal GET API호출 : 회원이 거래한 주식 정보를 출력");
        if (m == null){
            log.debug(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.ok().body(stockDealService.findAllByMemberNoAndStockDealType((int) m.getMemberNo(), stockdealtype));
        }
    }

//    @PostMapping
//    @Operation(summary = "회원 주식 거래 등록", description = "회원이 거래한 주식 정보를 등록한다.", tags = { "StockDeal" })
//    public ResponseEntity<Integer> save(@RequestHeader String Authorization, @RequestBody StockDealDto stockDealDto) {
//        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
//        if (m == null){
//            log.debug(ErrorMessage.TOKEN_EXPIRE);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } else {
//            stockDealDto.setMemberNo((int) m.getMemberNo());
//            return ResponseEntity.ok().body(stockDealService.save(stockDealDto));
//        }
//    }
//
//    @DeleteMapping
//    @Operation(summary = "회원 주식 거래 삭제", description = "회원이 거래한 주식 정보를 삭제한다.", tags = { "StockDeal" })
//    public ResponseEntity<?> delete(@RequestHeader String Authorization) {
//        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
//        if (m == null){
//            log.debug(ErrorMessage.TOKEN_EXPIRE);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } else {
//            if(stockDealService.delete(m.getMemberNo()) == 0){
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ReturnMessage.DELETE_FAIL);
//            }
//            return ResponseEntity.status(HttpStatus.OK).body(ReturnMessage.DELETE_SUCCESS);
//        }
//    }
}
