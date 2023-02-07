package com.udteam.miristock.service;

import com.udteam.miristock.dto.ArticleRequestDto;
import com.udteam.miristock.dto.ArticleResponseDto;
import com.udteam.miristock.dto.LimitPriceOrderDto;
import com.udteam.miristock.entity.ArticleEntity;
import com.udteam.miristock.entity.LimitPriceOrderEntity;
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

    public List<LimitPriceOrderDto> findAll(Integer memberNo) {
        List<LimitPriceOrderEntity> limitPriceOrderEntityList = limitPriceOrderRepository.findAllByMemberNo(memberNo);
        return limitPriceOrderEntityList.stream()
                .map(LimitPriceOrderDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public LimitPriceOrderDto save(LimitPriceOrderDto limitPriceOrderDto) {
        limitPriceOrderRepository.save(limitPriceOrderDto.toEntity());
        return limitPriceOrderDto;
    }

    @Transactional
    public void delete(Integer limitPriceOrderNo) {
        limitPriceOrderRepository.delete(LimitPriceOrderEntity.builder().limitPriceOrderNo(limitPriceOrderNo).build());
    }


}
