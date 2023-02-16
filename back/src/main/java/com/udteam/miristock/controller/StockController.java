//package com.udteam.miristock.controller;
//
//import com.udteam.miristock.service.StockService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@Slf4j
//@RestController
//@RequestMapping("/stock")
//@RequiredArgsConstructor
//public class StockController {
//
//    private final StockService searchService;
//
//    @GetMapping("/search")
//    public ResponseEntity<?> findByStockName (@RequestParam String keyword) throws Exception{
//        log.debug("종목검색 : {} :", keyword);
//        return ResponseEntity.ok().body(searchService.findByStockName(keyword));
//    }
//
//}
