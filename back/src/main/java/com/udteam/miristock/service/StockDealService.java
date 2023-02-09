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

    // 회원 거래내역 조회
    public List<StockDealDto> findAllByMemberNoAndStockDealType(Integer memberNo, Deal stockDealType) {
        List<StockDealEntity> stockDealEntityList = stockDealCustomRepository.findAllByMemberNoAndStockDealType(memberNo, stockDealType);
        return stockDealEntityList.stream()
                .map(StockDealDto::new)
                .collect(Collectors.toList());
    }

    // 회원 거래내역 등록
    @Transactional
    public Integer save(StockDealDto stockDealDto) {
        return stockDealRepository.save(stockDealDto.toEntity()).getStockDealNo();
    }

    // 회원 거래내역 삭제
    @Transactional
    public int delete(Integer memberNo) {
        return stockDealRepository.deleteAllByMemberNo(memberNo);
    }

}
