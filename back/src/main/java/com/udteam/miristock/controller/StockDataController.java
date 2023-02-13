package com.udteam.miristock.controller;

import com.udteam.miristock.dto.MemberAssetDto;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.dto.StockDataResponseDto;
import com.udteam.miristock.service.MemberAssetService;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.service.StockDataService;
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

@Slf4j
@RestController
@RequestMapping("/stockdata")
@RequiredArgsConstructor
@Tag(name = "StockData", description = "주식 데이터")
public class StockDataController {

    private final StockDataService stockDataService;
    private final MemberAssetService memberAssetService;
    private final MemberService memberService;

    @GetMapping("/amount/top/{stockDataDate}")
    @Operation(summary = "당일 거래량이 가장많은 5개 종목 출력", description = "당일 거래량이 가장많은 5개 종목 출력한다.", tags = { "StockData" })
    public ResponseEntity<List<StockDataResponseDto>> findTop5AmountDesc(@PathVariable Integer stockDataDate) {
        log.info("date : {} ", stockDataDate);
        return ResponseEntity.ok().body(stockDataService.findTop5AmountDesc(stockDataDate));
    }

    @GetMapping("/rate/decrease/{stockDataDate}")
    @Operation(summary = "당일 등락률이 가장높은 5개 종목 출력", description = "당일 등락률이 가장높은 5개 종목 출력한다.", tags = { "StockData" })
    public ResponseEntity<List<StockDataResponseDto>> findTop5FlucauationRateAsc(@PathVariable Integer stockDataDate) {
        log.info("date : {} ", stockDataDate);
        return ResponseEntity.ok().body(stockDataService.findTop5BySFlucauationRateAsc(stockDataDate));
    }

    @GetMapping("/rate/increase/{stockDataDate}")
    @Operation(summary = "당일 등락률이 가장낮은 5개 종목 출력", description = "당일 등락률이 가장낮은 5개 종목 출력한다.", tags = { "StockData" })
    public ResponseEntity<List<StockDataResponseDto>> findTop5FlucauationRateDesc(@PathVariable Integer stockDataDate) {
        log.info("date : {} ", stockDataDate);
        return ResponseEntity.ok().body(stockDataService.findTop5BySFlucauationRateDesc(stockDataDate));
    }

    @GetMapping("/detail")
    @Operation(summary = "주식데이터 기간별 출력", description = "기간별 주식 데이터를 출력한다.", tags = { "StockData" })
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
    @Operation(summary = "주식 종목 검색", description = "종목명, 코드 검색", tags = { "StockData" })
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
