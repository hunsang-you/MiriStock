package com.udteam.miristock.service;

import com.udteam.miristock.dto.StockDealDto;
import com.udteam.miristock.entity.Deal;
import com.udteam.miristock.entity.StockDealEntity;
import com.udteam.miristock.repository.StockDealCustomRepository;
import com.udteam.miristock.repository.StockDealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockDealService {

    private final StockDealRepository stockDealRepository;
    private final StockDealCustomRepository stockDealCustomRepository;

    public List<StockDealDto> findAllByMemberNoAndStockDealType(Integer memberNo, Deal stockDealType) {
        List<StockDealEntity> stockDealEntityList = stockDealCustomRepository.findAllByMemberNoAndStockDealType(memberNo, stockDealType);
        return stockDealEntityList.stream()
                .map(StockDealDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Integer save(StockDealDto stockDealDto) {
        return stockDealRepository.save(stockDealDto.toEntity()).getStockDealNo();
    }

    @Transactional
    public void delete(Integer memberNo) {
        stockDealRepository.deleteAllByMemberNo(memberNo);
    }
}
