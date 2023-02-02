package com.udteam.miristock.controller;


import com.udteam.miristock.dto.StockDataResponseDto;
import com.udteam.miristock.service.StockDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/stockdata")
@RequiredArgsConstructor
public class StockDataController {

    private final StockDataService stockDataService;

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


}
