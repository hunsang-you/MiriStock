package com.udteam.miristock.controller;


import com.udteam.miristock.dto.MemberAssetDto;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.dto.RequestSimulationDto;
import com.udteam.miristock.dto.StockDataResponseDto;
import com.udteam.miristock.service.MemberAssetService;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.service.StockDataService;
import com.udteam.miristock.util.ErrorMessage;
import com.udteam.miristock.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/stockdata")
@RequiredArgsConstructor
public class StockDataController {

    private final StockDataService stockDataService;
    private final MemberAssetService memberAssetService;
    private final MemberService memberService;

    @GetMapping("/amount/top/{stockDataDate}")
    public ResponseEntity<List<StockDataResponseDto>> findTop5AmountDesc(@PathVariable Integer stockDataDate) {
        log.info("date : {} ", stockDataDate);
        return ResponseEntity.ok().body(stockDataService.findTop5AmountDesc(stockDataDate));
    }

    @GetMapping("/rate/decrease/{stockDataDate}")
    public ResponseEntity<List<StockDataResponseDto>> findTop5FlucauationRateAsc(@PathVariable Integer stockDataDate) {
        log.info("date : {} ", stockDataDate);
        return ResponseEntity.ok().body(stockDataService.findTop5BySFlucauationRateAsc(stockDataDate));
    }

    @GetMapping("/rate/increase/{stockDataDate}")
    public ResponseEntity<List<StockDataResponseDto>> findTop5FlucauationRateDesc(@PathVariable Integer stockDataDate) {
        log.info("date : {} ", stockDataDate);
        return ResponseEntity.ok().body(stockDataService.findTop5BySFlucauationRateDesc(stockDataDate));
    }

    @GetMapping("/detail")
    public ResponseEntity<List<StockDataResponseDto>> findStockData(
            @RequestParam(value = "startDate") Integer searchStartDate,
            @RequestParam(value = "endDate" , required = false) Integer searchEndDate,
            @RequestParam(value = "stockCode") String searchStockCode
    ) {
        log.info("searchStartDate : {} ", searchStartDate);
        log.info("searchEndDate : {} ", searchEndDate);
        log.info("stockCode : {} ", searchStockCode);
        return ResponseEntity.ok().body(stockDataService.findStockData(searchStartDate, searchEndDate, searchStockCode));
    }

    @GetMapping("/search")
    public ResponseEntity<?> findByStockName (@RequestHeader String Authorization, @RequestParam String keyword) throws Exception{
        log.info("회원 시뮬레이션 날짜 기반 주식 종목 검색 호출됨");
        String token= HeaderUtil.getAccessTokenString(Authorization);
        MemberDto m = memberService.selectOneMember(token);
        MemberAssetDto result = null;
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }else {
            result = memberAssetService.selectMemberAsset(m.getMemberNo());
        }
        return ResponseEntity.status(HttpStatus.OK).body(stockDataService.findByStock(keyword , result.getMemberassetCurrentTime()));
    }

}
