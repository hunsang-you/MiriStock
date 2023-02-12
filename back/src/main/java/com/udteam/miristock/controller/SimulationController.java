package com.udteam.miristock.controller;

import com.udteam.miristock.dto.*;
import com.udteam.miristock.service.*;
import com.udteam.miristock.util.ErrorMessage;
import com.udteam.miristock.util.HeaderUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Simulation", description = "주식 시뮬레이션")
public class SimulationController {
    private final MemberAssetService memberAssetService;
    private final MemberService memberService;
    private final SimulationService simulationService;
    private final StockDataService stockDataService;
    private final LimitPriceOrderService limitPriceOrderService;

    @GetMapping("/member/time")
    @Operation(summary = "회원 시뮬레이션 날짜 출력", description = "회원의 시뮬레이션 날짜를 출력한다.", tags = { "Simulation" })
    public ResponseEntity<?> selectMemberAssetCurrentTime(@RequestHeader String Authorization) {
        log.info("회원 시뮬레이션 날짜 출력 호출됨 (/asset/member/time) ");
        String token= HeaderUtil.getAccessTokenString(Authorization);
        MemberDto m = memberService.selectOneMember(token);
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.TOKEN_EXPIRE);
        }
        MemberAssetDto result = memberAssetService.selectMemberAsset(m.getMemberNo());
        return ResponseEntity.status(HttpStatus.OK)
                .body(RequestSimulationDto.builder()
                        .memberNo(result.getMemberNo())
                        .memberassetCurrentTime(result.getMemberassetCurrentTime())
                        .build());
    }

    @PostMapping("/end")
    @Operation(summary = "회원 시뮬레이션 종료", description = "회원의 시뮬레이션을 종료한다. (거래예정 취소, 보유주식 목록 강제 판매됨)", tags = { "Simulation" })
        public ResponseEntity<SimulEndDto> resultSimulation(@RequestHeader String Authorization) {
        log.info("회원 시뮬레이션 종료 호출됨 ");
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok().body(simulationService.resultSimulation(m.getMemberNo()));
    }

    @PostMapping("/restart")
    @Operation(summary = "회원 시뮬레이션 초기화", description = "회원의 시뮬레이션을 초기화한다. (시뮬레이션관련 DB 초기화)", tags = { "Simulation" })
    public ResponseEntity<?> restartSimulation(@RequestHeader String Authorization) {
        log.info("회원 시뮬레이션 초기화 됨 ");
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.TOKEN_EXPIRE);
        }
        simulationService.resetSimulation(m);
        return ResponseEntity.ok().body("Simulation RESET Success");
    }

    @PutMapping("/member/time/{targetDate}")
    @Operation(summary = "회원 시뮬레이션 날짜 이동 (내부 로직 동작)", description = "회원의 시뮬레이션 날짜를 이동한다.", tags = { "Simulation" })
    public ResponseEntity<?> updateSimulationDate(@RequestHeader String Authorization, @PathVariable Integer targetDate) {
        if(targetDate == null || targetDate == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parameter is Null");
        }
        log.info("회원 시뮬레이션 날짜 변경 호출됨 -> 추가할 날짜  : {}", targetDate);
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.TOKEN_EXPIRE);
        }
        // 회원 시뮬레이션 시간 가져오기
        MemberAssetDto getMemberAssetDto = memberAssetService.selectMemberAsset(m.getMemberNo());
        Integer memberDate = getMemberAssetDto.getMemberassetCurrentTime();
        log.info("회원의 시뮬레이션 시간 : {}", memberDate);
        
        // 시간 추가 로직
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
                    limitPriceOrderService.oneLimitPriceOrderSave(limitPriceOrderDto, getMemberAssetDto);
                }
                // 모든 거래 이후에 회원 주식자산을 업데이트한다.
                // 주식종목의 평균매입가와 현재종가로 비교하여 회원 주식 자산을 업데이트 한다.
                memberAssetService.updateMemberStockAsset(m.getMemberNo(), memberDate, "Simulation");
                dayCount++;
            }
        }
        return ResponseEntity.ok().body("Time Move Success");
    }

    @PutMapping("/member/timechange/debug/{targetDate}")
    @Operation(summary = "회원 시뮬레이션 날짜 이동 (내부 로직 미 동작)", description = "회원의 시뮬레이션 날짜를 단순히 수정한다.", tags = { "Simulation" })
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

//    @GetMapping("/test")
//    public ResponseEntity<?> test(@RequestHeader String Authorization) {
//        log.info(" 테스트 메서드 호출됨. ");
//        String token= HeaderUtil.getAccessTokenString(Authorization);
//        MemberDto m = memberService.selectOneMember(token);
//        if (m == null){
//            log.info(ErrorMessage.TOKEN_EXPIRE);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }else {
//            MemberAssetDto result = memberAssetService.selectMemberAsset(m.getMemberNo());
////            memberStockService.updateMemberStockAsset(m.getMemberNo(), result.getMemberassetCurrentTime());
//            return ResponseEntity.ok().body(memberAssetService.updateMemberStockAsset(m.getMemberNo(), result.getMemberassetCurrentTime()));
//        }
//    }

}
