package com.udteam.miristock.service;

import com.udteam.miristock.dto.ArticleRequestDto;
import com.udteam.miristock.dto.ArticleResponseDto;
import com.udteam.miristock.dto.LimitPriceOrderDto;
import com.udteam.miristock.entity.ArticleEntity;
import com.udteam.miristock.entity.Deal;
import com.udteam.miristock.entity.LimitPriceOrderEntity;
import com.udteam.miristock.repository.LimitPriceOrderCustomRepository;
import com.udteam.miristock.repository.LimitPriceOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LimitPriceOrderService {

    private final LimitPriceOrderRepository limitPriceOrderRepository;
    private final LimitPriceOrderCustomRepository limitPriceOrderCustomRepository;

    public List<LimitPriceOrderDto> findAll(Integer memberNo, Deal limitPriceOrderType) {
        List<LimitPriceOrderEntity> limitPriceOrderEntityList = limitPriceOrderCustomRepository.findAllByMemberNoAndLimitPriceOrderType(memberNo, limitPriceOrderType);
        return limitPriceOrderEntityList.stream()
                .map(LimitPriceOrderDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public LimitPriceOrderDto save(LimitPriceOrderDto limitPriceOrderDto) {
        return new LimitPriceOrderDto(limitPriceOrderRepository.saveAndFlush(limitPriceOrderDto.toEntity()));
    }

    @Transactional
    public void delete(Integer memberNo, Integer limitPriceOrderNo) {
        limitPriceOrderRepository.deleteAllByMemberNoAndLimitPriceOrderNo(memberNo, limitPriceOrderNo);
    }


}
