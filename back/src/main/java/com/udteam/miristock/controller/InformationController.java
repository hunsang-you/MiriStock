package com.udteam.miristock.controller;

import com.udteam.miristock.dto.FinancialstatementDto;
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
         return ResponseEntity.ok().body(informationService.findAllFinancialstatement(String.valueOf(stockCode)));
    }
}
