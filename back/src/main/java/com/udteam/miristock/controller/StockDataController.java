package com.udteam.miristock.controller;


import com.udteam.miristock.dto.StockDataResponseDto;
import com.udteam.miristock.service.StockDataService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/stockdata")
public class StockDataController {

    private Logger logger = LoggerFactory.getLogger(StockDataController.class);
    private final StockDataService stockDataService;

    @GetMapping("/hello")
    public ResponseEntity<String> fffff() {
        logger.info("test");
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/rate/decrease/{stockDataDate}")
    public ResponseEntity<List<StockDataResponseDto>> findTop5FlucauationRateAsc(@PathVariable int stockDataDate) {
        logger.info("date : {} ", stockDataDate);
        return ResponseEntity.ok().body(stockDataService.findTop5BySFlucauationRateAsc(stockDataDate));
    }

    @GetMapping("/rate/increase/{stockDataDate}")
    public ResponseEntity<List<StockDataResponseDto>> findTop5FlucauationRateDesc(@PathVariable int stockDataDate) {
        logger.info("date : {} ", stockDataDate);
        return ResponseEntity.ok().body(stockDataService.findTop5BySFlucauationRateDesc(stockDataDate));
    }

    @GetMapping("/detail")
    public ResponseEntity<List<StockDataResponseDto>> findStockData(
            @RequestParam(value = "startDate") Integer searchStartDate,
            @RequestParam(value = "endDate" , required = false) Integer searchEndDate,
            @RequestParam(value = "stockCode") String searchStockCode
    ) {
        logger.info("searchStartDate : {} ", searchStartDate);
        logger.info("searchEndDate : {} ", searchEndDate);
        logger.info("stockCode : {} ", searchStockCode);
        return ResponseEntity.ok().body(stockDataService.findStockData(searchStartDate, searchEndDate, searchStockCode));
    }


}
