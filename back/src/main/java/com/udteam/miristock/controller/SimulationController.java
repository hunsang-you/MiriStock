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
//    private final MemberStockService memberStockService;

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

    @PostMapping("/restart")
    public ResponseEntity<?> restartSimulation(@RequestHeader String Authorization) {
        log.info("회원 시뮬레이션 초기화 됨 ");
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            simulationService.resetSimulation(m);
            return ResponseEntity.ok().body("Reset");
        }
    }

    @PutMapping("/member/time/{targetDate}")
    public ResponseEntity<?> updateSimulationDate(@RequestHeader String Authorization, @PathVariable Integer targetDate) {
        if(targetDate == null || targetDate == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parameter is Null");
        }
        log.info("회원 시뮬레이션 날짜 변경 호출됨 -> 추가할 날짜  : {}", targetDate);
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Integer memberDate = memberAssetService.selectMemberAsset(m.getMemberNo()).getMemberassetCurrentTime();
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
                // 예정 거래 내역들과 현재 주식 종가와 비교하여 거래하기
                List<LimitPriceOrderDto> getLimitList =  limitPriceOrderService.getLimitPriceOrderAllList(m.getMemberNo());
                for (LimitPriceOrderDto limitPriceOrderDto : getLimitList) {
                    limitPriceOrderService.oneLimitPriceOrderSave(limitPriceOrderDto, memberDate);
                }
                // 모든 거래 이후에 회원 주식자산을 업데이트한다.
                // 주식종목의 평균매입가와 현재종가로 비교하여 회원 주식 자산을 업데이트 한다.
                memberAssetService.updateMemberStockAsset(m.getMemberNo(), memberDate);
                dayCount++;
            }
        }
        return ResponseEntity.ok().body("Time Move Success");
    }

    @PutMapping("/member/timechange/debug/{targetDate}")
    public ResponseEntity<?> changeSimulTime(@RequestHeader String Authorization, @PathVariable(name = "targetDate") Integer targetDate) {
        log.info("회원 시뮬레이션 날짜 단순 변경 호출됨 (/asset/member/time) targetDate : {}", targetDate);
        String token= HeaderUtil.getAccessTokenString(Authorization);
        MemberDto m = memberService.selectOneMember(token);
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }else {
            MemberAssetDto result = memberAssetService.selectMemberAsset(m.getMemberNo());
            log.info("before result : {} ", result);
            result.setMemberassetCurrentTime(targetDate);
            log.info("after result : {} ", result);
            if (memberAssetService.updateMemberAsset(result) == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Member Simulation Time Change Fail");
            }
            return ResponseEntity.status(HttpStatus.OK).body("Member Simulation Time Change Success! -> " + targetDate);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestHeader String Authorization) {
        log.info(" 테스트 메서드 호출됨. ");
        String token= HeaderUtil.getAccessTokenString(Authorization);
        MemberDto m = memberService.selectOneMember(token);
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }else {
            MemberAssetDto result = memberAssetService.selectMemberAsset(m.getMemberNo());
//            memberStockService.updateMemberStockAsset(m.getMemberNo(), result.getMemberassetCurrentTime());
            return ResponseEntity.ok().body(memberAssetService.updateMemberStockAsset(m.getMemberNo(), result.getMemberassetCurrentTime()));
        }
    }

}
