package com.udteam.miristock.service;

import com.udteam.miristock.dto.InterestDto;
import com.udteam.miristock.dto.StockDataResponseDto;
import com.udteam.miristock.entity.InterestEntity;
import com.udteam.miristock.entity.StockDataEntity;
import com.udteam.miristock.repository.InterestRepository;
import com.udteam.miristock.repository.MemberRepository;
import com.udteam.miristock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterestService {
    private final InterestRepository interestRepository;
    private final MemberRepository memberRepository;
    private final StockRepository stockRepository;

    public List<StockDataResponseDto> selectInterestStock(Integer stockDataDate, Integer memberNo){
        List<StockDataEntity> stockDataEntityList = interestRepository.selectInterestStock(stockDataDate, memberNo);
        return stockDataEntityList.stream().map(StockDataResponseDto::new).collect(Collectors.toList());
    }

    public List<InterestDto> selectInterestStockNoDate(Integer memberNo){
        List<InterestEntity> interestEntity = interestRepository.findByMember_MemberNo(memberNo);
        return interestEntity.stream().map(InterestDto::of).collect(Collectors.toList());
    }

    public Object insertIntereststock(Integer id, String stockCode) {
        List<InterestEntity> interestEntity = interestRepository.findByMember_MemberNo(id);
        for (InterestEntity entity : interestEntity) {
            if (entity.getStock().getStockCode().equals(stockCode)) {
                return "Duplicated StockCode : save suspend";
            }
        }
        return InterestDto.of(interestRepository.saveAndFlush(InterestEntity.builder()
                .member(memberRepository.findById(id).get())
                .stock(stockRepository.findById(stockCode).get())
                .build()));
    }

    public int deleteIntereststock(Integer id, String stockCode){
        return interestRepository.deleteByMember_MemberNoAndStock_StockCode(id,stockCode);
    }
}

