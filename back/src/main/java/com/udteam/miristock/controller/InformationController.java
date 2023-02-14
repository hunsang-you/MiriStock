package com.udteam.miristock.controller;

import com.udteam.miristock.dto.FinancialstatementDto;
import com.udteam.miristock.dto.NewsRequestDto;
import com.udteam.miristock.dto.NewsResponseDto;
import com.udteam.miristock.service.InformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
@Tag(name = "Information", description = "주식 관련 정보 제공")
public class InformationController {
    private final InformationService informationService;
    
    @GetMapping("/financialstatement/{stockCode}")
    @Operation(summary = "재무재표 출력", description = "주식종목에 대한 제무재표를 출력한다.", tags = { "Information" })
    public ResponseEntity<List<FinancialstatementDto>> findAllFinancialstatement(@PathVariable String stockCode) {
        log.info("재무재표 호출 요청됨 : code -> {} ", stockCode);
         return ResponseEntity.ok().body(informationService.findAllFinancialstatement(stockCode));
    }

    @PostMapping("/news")
    @Operation(summary = "뉴스 출력", description = "주식종목에 대한 뉴스를 출력한다.", tags = { "Information" })
    public ResponseEntity<NewsResponseDto> findNews(@RequestBody NewsRequestDto newsRequestDto) {
        log.info("뉴스 호출 요청됨 : newsRequestDto -> {}", newsRequestDto);
        return ResponseEntity.ok().body(informationService.findNaverNews(newsRequestDto));
    }
}
