package com.udteam.miristock.service;

import com.udteam.miristock.dto.InterestDto;
import com.udteam.miristock.entity.InterestEntity;
import com.udteam.miristock.repository.InterestRepository;
import com.udteam.miristock.repository.MemberRepository;
import com.udteam.miristock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterestService {
    private final InterestRepository interestRepository;
    private final MemberRepository memberRepository;
    private final StockRepository stockRepository;

    public List<InterestDto> selectMemberStock(int id){
        return interestRepository.findAllById(Collections.singleton(id))
                .stream()
                .map(InterestDto::of)
                .collect(Collectors.toList());
    }

    public InterestDto insertIntereststock(long id,String stockCode) {
        return InterestDto.of(interestRepository.saveAndFlush(InterestEntity.builder()
                .member(memberRepository.findById(id).get())
                .stock(stockRepository.findById(stockCode).get())
                .build()));
    }

    public String deleteIntereststock(int id,String stockCode){
        interestRepository.deleteByMember_MemberNoAndStock_StockCode(id,stockCode);
        return "삭제 완료";
    }
}

