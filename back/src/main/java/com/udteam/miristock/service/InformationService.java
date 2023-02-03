package com.udteam.miristock.service;

import com.udteam.miristock.dto.FinancialstatementDto;
import com.udteam.miristock.dto.LimitPriceOrderDto;
import com.udteam.miristock.entity.FinancialstatementEntity;
import com.udteam.miristock.entity.LimitPriceOrderEntity;
import com.udteam.miristock.repository.FinancialstatementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InformationService {

    private final FinancialstatementRepository financialstatementRepository;

    public List<FinancialstatementDto> findAllFinancialstatement(String stockCode) {
        return financialstatementRepository.findByStockCode(stockCode)
                .stream()
                .map(FinancialstatementDto::new)
                .collect(Collectors.toList());
    }

}
