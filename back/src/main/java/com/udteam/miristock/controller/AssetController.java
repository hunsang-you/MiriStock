package com.udteam.miristock.controller;

import com.udteam.miristock.dto.InterestDto;
import com.udteam.miristock.dto.MemberAssetDto;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.dto.StockDataResponseDto;
import com.udteam.miristock.service.InterestService;
import com.udteam.miristock.service.MemberAssetService;
import com.udteam.miristock.service.MemberService;
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
public class AssetController {

    private final MemberAssetService memberAssetService;
    private final InterestService interestService;
    private final MemberService memberService;

    @GetMapping
    @ApiOperation(value = "회원 보유 자산 불러오기")
    public ResponseEntity<MemberAssetDto> selectMemberAsset(@RequestHeader String Authorization) {
        String token= HeaderUtil.getAccessTokenString(Authorization);
        MemberDto m = memberService.selectOneMember(token);
        if (m == null){
            log.info("토큰 기간이 만료되었거나 유저 정보가 존재하지 않습니다");
            // 엑세스 토큰 재 발급 프로세스 필요
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }else {
            return ResponseEntity.ok()
                    .body(memberAssetService.selectMemberAsset((int) m.getMemberNo()));
        }
    }

    @GetMapping("/stockholding")
    @ApiOperation(value = " 회원 보유 주식 목록 출력(평가금액 순)")
    public ResponseEntity selectMemberStock(@RequestHeader String Authorization) {
        String token= HeaderUtil.getAccessTokenString(Authorization);
        MemberDto m = memberService.selectOneMember(token);
//        if(m==null ){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(null);
//        }else {
//            return ResponseEntity.ok()
//                    .body(interestService.selectMemberStock(m.getMemberNo()));
//        }
        return null;
    }

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

    @PostMapping("/intereststock")
    @ApiOperation(value = "회원 관심주식 추가")
    public ResponseEntity<InterestDto> insertIntereststock(@RequestHeader String Authorization, @RequestParam String stockcode) {
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            interestService.deleteIntereststock(m.getMemberNo(), stockcode);
            return ResponseEntity.ok().body(ReturnMessage.DELETE_SUCCESS);
        }
    }

}
