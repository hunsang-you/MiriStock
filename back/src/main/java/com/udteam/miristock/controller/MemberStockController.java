package com.udteam.miristock.controller;

import com.udteam.miristock.dto.*;
import com.udteam.miristock.service.MemberAssetService;
import com.udteam.miristock.util.ErrorMessage;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.service.MemberStockService;
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
@RequestMapping("/asset/memberstock")
@RequiredArgsConstructor
@Tag(name = "MemberStock", description = "회원 보유 주식")
public class MemberStockController {

    private final MemberStockService memberStockService;
    private final MemberService memberService;
    private final MemberAssetService memberAssetService;

    @GetMapping()
    @Operation(summary = "보유 주식 목록 출력", description = "회원의 보유 주식 목록과 해당 시뮬레이션 날짜의 주식 정보도 출력한다.", tags = { "MemberStock" })
    public ResponseEntity<?> findAll(@RequestHeader String Authorization, @RequestParam(required=false, defaultValue="price") String type) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            MemberAssetDto result = memberAssetService.selectMemberAsset(m.getMemberNo());
            return ResponseEntity.ok().body(memberStockService.findAll(m.getMemberNo(), result.getMemberassetCurrentTime(), type));
        }
    }
    
    @GetMapping("/{stockCode}")
    @Operation(summary = "보유 주식 단건 검색", description = "회원의 보유 주식 목록에서 코드 검색을 통해 단건 출력한다.", tags = { "MemberStock" })
    public ResponseEntity<?> findOne(@RequestHeader String Authorization, @PathVariable String stockCode) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            MemberAssetDto result = memberAssetService.selectMemberAsset(m.getMemberNo());
            return ResponseEntity.ok().body(memberStockService.findOne(m.getMemberNo(), result.getMemberassetCurrentTime(), stockCode).get(0));
        }
    }

    // 전날 대비 보유주식 등락률 보여주기
    @GetMapping("/main")
    @Operation(summary = "전일 대비 보유주식에 대한 등락률, 등락금액 출력", description = "전일 대비 회원 보유주식의 등락률, 등락금액 출력", tags = { "MemberStock" })
    public ResponseEntity<StockRateAndPriceResponseDto> getMemberStockRateAndPrice(@RequestHeader String Authorization) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            MemberAssetDto result = memberAssetService.selectMemberAsset(m.getMemberNo());
            return ResponseEntity.ok().body(memberStockService.getMemberStockRateAndPrice(m.getMemberNo(), result.getMemberassetCurrentTime()));
        }
    }

//    // 보유 주식 등록 테스트
//    @PostMapping
//    public ResponseEntity<MemberStockDto> save(@RequestHeader String Authorization, @RequestBody MemberStockDto memberStockDto) {
//        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
//        if (m == null){
//            log.info(ErrorMessage.TOKEN_EXPIRE);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } else {
//            memberStockDto.setMemberNo(m.getMemberNo());
//            return ResponseEntity.ok().body(memberStockService.save(memberStockDto));
//        }
//    }
//
//    // 보유 주식 수정 테스트
//    @PutMapping
//    public ResponseEntity<MemberStockDto> update(@RequestHeader String Authorization, @RequestBody MemberStockDto memberStockDto) {
//        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
//        if (m == null){
//            log.info(ErrorMessage.TOKEN_EXPIRE);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } else {
//            memberStockDto.setMemberNo(m.getMemberNo());
//            return ResponseEntity.ok().body(memberStockService.save(memberStockDto));
//        }
//    }
//
//    // 보유 주식 삭제 테스트
//    @DeleteMapping
//    public ResponseEntity<?> delete(@RequestHeader String Authorization, @RequestParam String stockCode) {
//        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
//        if (m == null){
//            log.info(ErrorMessage.TOKEN_EXPIRE);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } else {
//            if (memberStockService.delete(m.getMemberNo(), stockCode) == 0){
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ReturnMessage.DELETE_FAIL);
//            }
//            return ResponseEntity.status(HttpStatus.OK).body(ReturnMessage.DELETE_SUCCESS);
//        }
//    }

}
