package com.udteam.miristock.controller;

import com.udteam.miristock.dto.InterestDto;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.dto.StockDataResponseDto;
import com.udteam.miristock.service.InterestService;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.util.ErrorMessage;
import com.udteam.miristock.util.HeaderUtil;
import com.udteam.miristock.util.ReturnMessage;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;
    private final MemberService memberService;

    @GetMapping("/intereststock/{stockDate}")
    @ApiOperation(value = "회원 관심 주식 목록 출력")
    public ResponseEntity<List<StockDataResponseDto>> selectIntereststcok(@RequestHeader String Authorization, @PathVariable Integer stockDate) {
        log.info("회원 관심 주식 목록 출력 호출됨 / 날짜 : {} ", stockDate);
        String token= HeaderUtil.getAccessTokenString(Authorization);
        MemberDto m = memberService.selectOneMember(token);
        if (m==null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }else{
            log.info("회원 고유 식별번호 : {}", m.getMemberNo());
            log.info("날짜 : {}", stockDate);
            return ResponseEntity.ok().body(interestService.selectInterestStock(stockDate, m.getMemberNo()));
        }
    }

    @GetMapping("/intereststock/list")
    @ApiOperation(value = "회원 관심 주식 목록 단순 출력")
    public ResponseEntity<?> selectIntereststcok(@RequestHeader String Authorization) {
        log.info("회원 관심 단순 주식 목록 출력 호출됨");
        String token= HeaderUtil.getAccessTokenString(Authorization);
        MemberDto m = memberService.selectOneMember(token);
        if (m==null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }else{
            log.info("회원 고유 식별번호 : {}", m.getMemberNo());
            return ResponseEntity.ok().body(interestService.selectInterestStockNoDate(m.getMemberNo()));
        }
    }

    @PostMapping("/intereststock")
    @ApiOperation(value = "회원 관심주식 추가")
    public ResponseEntity<?> insertIntereststock(@RequestHeader String Authorization, @RequestParam String stockcode) {
        String token= HeaderUtil.getAccessTokenString(Authorization);
        MemberDto m = memberService.selectOneMember(token);
        if (m == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.ok().body(interestService.insertIntereststock(m.getMemberNo(), stockcode));
        }
    }

    @DeleteMapping("/intereststock")
    @ApiOperation(value = "회원 관심주식 제거")
    public ResponseEntity<String> deleteIntereststock(@RequestHeader String Authorization,@RequestParam String stockcode) {
        String token= HeaderUtil.getAccessTokenString(Authorization);
        MemberDto m = memberService.selectOneMember(token);
        if (m == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.TOKEN_EXPIRE);
        }

        if (interestService.deleteIntereststock(m.getMemberNo(), stockcode) == 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ReturnMessage.DELETE_FAIL);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ReturnMessage.DELETE_SUCCESS);

    }
}
