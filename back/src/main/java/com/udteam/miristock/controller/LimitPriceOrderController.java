package com.udteam.miristock.controller;

import com.udteam.miristock.dto.MemberAssetDto;
import com.udteam.miristock.service.MemberAssetService;
import com.udteam.miristock.util.ErrorMessage;
import com.udteam.miristock.dto.LimitPriceOrderDto;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.entity.Deal;
import com.udteam.miristock.service.LimitPriceOrderService;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.util.HeaderUtil;
import com.udteam.miristock.util.ReturnMessage;
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
@RequestMapping("/limitpriceorder")
@RequiredArgsConstructor
@Tag(name = "LimitPriceOrder", description = "매수/매도 예약")
public class LimitPriceOrderController {

    private final LimitPriceOrderService limitPriceOrderService;
    private final MemberAssetService memberAssetService;
    private final MemberService memberService;
    
    @GetMapping
    @Operation(summary = "매수/매도 예정 내역 출력", description = "회원의 매수/매도 예정 목록을 출력한다.", tags = { "LimitPriceOrder" })
    public ResponseEntity<List<LimitPriceOrderDto>> findAll(@RequestHeader String Authorization, @RequestParam(required = false) Deal limitPriceOrderType) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        log.info("회원 : {} | /limitpriceorder GET API호출 : 매수/매도 예정 내역 출력", m);
        if (m == null){
            log.debug(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(limitPriceOrderService.findAll(m.getMemberNo(), limitPriceOrderType));
        }
    }

    /**
     * 단건 매도 / 매수 요청시 현재 시뮬레이션 날짜의 가격과 비교
     * 1. 예상 매수가(구입가)가 현재 종가보다 높다면 구매한다.
     *   -> 거래내역(BUY타입으로) 테이블에 등록한다.
     *   -> 보유주식목록에 거래량, 구입가, 구입날짜(시뮬레이션시간) 반영한다.
     * 2. 예상 매수가(구입가)가 현재 종가보다 낮다면 매수 예정 테이블에 등록한다.
     */
    // 단건 매수 매도 발생 발생...
    @PostMapping
    @Operation(summary = "매수/매도 예정 등록", description = "회원의 매수/매도 요청을 등록한다.", tags = { "LimitPriceOrder" })
    public ResponseEntity<?> save(@RequestHeader String Authorization, @RequestBody LimitPriceOrderDto limitPriceOrderDto) {
        // 회원 정보 불러오기
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        log.info("회원 : {} | /limitpriceorder POST API호출 : 매수/매도 예정 내역 등록", m);
        // 회원의 시뮬레이션 시간 불러오기
        MemberAssetDto getMemberAssetDto = memberAssetService.selectMemberAsset(m.getMemberNo());
        Integer memberDate = getMemberAssetDto.getMemberassetCurrentTime();
        String updateType = "todayLimitPriceOrder";

        // 이 부분을 왜 넣었을까?
//        memberAssetService.updateMemberStockAsset(m.getMemberNo(), memberDate, updateType);
        log.debug("매수, 매도 요청 등록됨 limitPriceOrderDto {} , ", limitPriceOrderDto);

        if (m == null){
            log.debug(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            limitPriceOrderDto.setMemberNo(m.getMemberNo());
            // 요청값, 시간 넣고 로직 실행
            // 만약 잘못되었다면... 에러..
            Object result = limitPriceOrderService.oneLimitPriceOrderSave(limitPriceOrderDto, getMemberAssetDto);
//            if(result == null){
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR");
//            } else {
                return ResponseEntity.status(HttpStatus.OK).body(result);
//            }
        }
    }


    // 단건 매수/매도 내역 수정 -> 등록했을때 작업을 또 비교해야하네
    @PutMapping
    @Operation(summary = "매수/매도 예정 수정", description = "회원의 매수/매도 요청을 수정한다.", tags = { "LimitPriceOrder" })
    public ResponseEntity<?> update(@RequestHeader String Authorization, @RequestBody LimitPriceOrderDto limitPriceOrderDto) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        // 회원 정보 불러오기
        MemberAssetDto memberAssetDto = memberAssetService.selectMemberAsset(m.getMemberNo());
        log.info("회원 : {} | /limitpriceorder PUT API호출 : 매수/매도 예정 내역 수정", m);
        log.debug("매수, 매도 수정 등록됨 limitPriceOrderDto {} , ", limitPriceOrderDto);
        if (m == null){
            log.debug(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            limitPriceOrderDto.setMemberNo(m.getMemberNo());
            return ResponseEntity.status(HttpStatus.OK).body(limitPriceOrderService.oneLimitPriceOrderUpdate(limitPriceOrderDto, memberAssetDto));
        }
    }

    // 매수, 매도 내역에서 삭제...
    @DeleteMapping("/{limitPriceOrderNo}")
    @Operation(summary = "매수/매도 예정 삭제", description = "회원의 매수/매도 요청을 삭제한다.", tags = { "LimitPriceOrder" })
    public ResponseEntity<String> delete(@RequestHeader String Authorization, @PathVariable Integer limitPriceOrderNo) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        log.info("회원 : {} | /limitpriceorder/{limitPriceOrderNo} DELETE API호출 : 매수/매도 예정 내역 삭제", m);
        if (m == null){
            log.debug(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            if (limitPriceOrderService.delete(m.getMemberNo(), limitPriceOrderNo) == 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ReturnMessage.DELETE_FAIL);
            }
            return ResponseEntity.status(HttpStatus.OK).body(ReturnMessage.DELETE_SUCCESS);
        }
    }

}
