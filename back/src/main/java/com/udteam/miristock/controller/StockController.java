package com.udteam.miristock.controller;

import com.udteam.miristock.entity.Stock;
import com.udteam.miristock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stock")
public class StockController {

    private final Logger logger = LoggerFactory.getLogger(StockController.class);

    private final StockService searchService;

    @GetMapping("/search")
    public ResponseEntity<List<Stock>> findByStockName (@RequestParam String stockName) throws Exception{
        logger.info("종목검색 : {} :", stockName);
        return ResponseEntity.ok().body(searchService.findByStockName(stockName));
    }

}
