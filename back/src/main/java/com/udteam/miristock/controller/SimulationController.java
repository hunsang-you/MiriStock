package com.udteam.miristock.controller;

import com.udteam.miristock.dto.*;
import com.udteam.miristock.entity.Deal;
import com.udteam.miristock.service.*;
import com.udteam.miristock.util.ErrorMessage;
import com.udteam.miristock.util.HeaderUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.udteam.miristock.service.InformationService.AddDate;

@Slf4j
@RestController
@RequestMapping("/simulation")
@RequiredArgsConstructor
public class SimulationController {
    private final MemberAssetService memberAssetService;
    private final MemberService memberService;
    private final SimulationService simulationService;
    private final StockDataService stockDataService;
    private final LimitPriceOrderService limitPriceOrderService;

    @GetMapping("/member/time")
    public ResponseEntity<RequestSimulationDto> selectMemberAssetCurrentTime(@RequestHeader String Authorization) {
        log.info("회원 시뮬레이션 날짜 출력 호출됨 (/asset/member/time) ");
        String token= HeaderUtil.getAccessTokenString(Authorization);
        MemberDto m = memberService.selectOneMember(token);
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }else {
            MemberAssetDto result = memberAssetService.selectMemberAsset(m.getMemberNo());
            return ResponseEntity.ok().body(RequestSimulationDto.builder().memberNo(result.getMemberNo()).memberassetCurrentTime(result.getMemberassetCurrentTime()).build());
        }
    }

    // resultSimulation
    @PostMapping("/end")
        public ResponseEntity<SimulEndDto> resultSimulation(@RequestHeader String Authorization) {
        log.info("회원 시뮬레이션 종료 호출됨 ");
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.ok().body(simulationService.resultSimulation(m.getMemberNo()));
        }
    }
    @PutMapping("/member/time/{date}")
    public ResponseEntity<?> updateSimulationDate(@RequestHeader String Authorization, @PathVariable Integer date) {
        log.info("회원 시뮬레이션 날짜 변경 호출됨 -> 추가할 날짜  : {}", date);
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));

        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            // simulationService
            return null;
//            return ResponseEntity.ok().body(memberAssetService.updateMemberAssetTime(requestSimulationDto));
        }
    }

    // 쿼리문 테스트용..
    @GetMapping("/hello/{date}")
    public ResponseEntity<?> test(@PathVariable Integer targetDate) {

        // @RequestHeader String Authorization
         MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        Integer memberDate = memberAssetService.selectMemberAsset(1).getMemberassetCurrentTime();
        log.info("회원의 시뮬레이션 시간 : {}", memberDate);
        String memberDateStr = null;

        int dayCount = 0;
        // 날짜 먼저 더하기
        while(true) {
            memberDateStr = AddDate(String.valueOf(memberDate), 0, 0, 1);
            // 해당 날짜 데이터가 있는지 확인하기
            memberDate = Integer.parseInt(memberDateStr);
            StockDataInfoMapping result = stockDataService.findTop1ByStockDataDate(memberDate);
            if (result == null) {
                log.info("날짜 데이터 체크 -> null 입니다.");
            } else {
                if(dayCount == targetDate){
                    break;
                }
                log.info("날짜 데이터 체크 : {}", result.getStockDataDate());
                List<LimitPriceOrderDto> getLimitList =  limitPriceOrderService.getLimitPriceOrderAllList(m.getMemberNo());
                for (LimitPriceOrderDto limitPriceOrderDto : getLimitList) {
                    limitPriceOrderService.oneLimitPriceOrderSave(limitPriceOrderDto, memberDate);
                }
                dayCount++;
            }
        }

        return null;
    }


//    @PutMapping("/member/time")
//    public ResponseEntity<SimulationDto> update(@RequestHeader String Authorization, @RequestBody MemberAssetDto memberAssetDto) {
//        log.info("회원 시뮬레이션 날짜 변경 호출됨  : {}", memberAssetDto);
//        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
//        if (m == null){
//            log.info(ErrorMessage.TOKEN_EXPIRE);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } else {
//            return ResponseEntity.ok().body(memberAssetService.updateMemberAssetTime(memberAssetDto));
//        }
//    }

}
