package com.udteam.miristock.controller;

import com.udteam.miristock.dto.FinancialstatementDto;
import com.udteam.miristock.dto.NewsRequestDto;
import com.udteam.miristock.dto.NewsResponseDto;
import com.udteam.miristock.service.InformationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
public class InformationController {
    private final InformationService informationService;
    @GetMapping("/financialstatement/{stockCode}")
    public ResponseEntity<List<FinancialstatementDto>> findAllFinancialstatement(@PathVariable String stockCode) {
        log.info("재무재표 호출 요청됨 : code -> {} ", stockCode);
         return ResponseEntity.ok().body(informationService.findAllFinancialstatement(stockCode));
    }

    @PostMapping("/news")
    public ResponseEntity<NewsResponseDto> findNews(@RequestBody NewsRequestDto newsRequestDto) {
        log.info("뉴스 호출 요청됨 : newsRequestDto -> {}", newsRequestDto);
        return ResponseEntity.ok().body(informationService.findNews(newsRequestDto));
    }
}
