package com.udteam.miristock.service;

import com.udteam.miristock.dto.*;
import com.udteam.miristock.entity.*;
import com.udteam.miristock.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.udteam.miristock.service.InformationService.AddDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulationService {
    private final MemberAssetRepository memberAssetRepository;
    private final MemberStockRepository memberStockRepository;
//    private final StockDataRepository stockDataRepository;
    private final LimitPriceOrderRepository limitPriceOrderRepository;
//    private final StockDealRepository stockDealRepository;

    @Transactional(readOnly = true)
    public SimulEndDto resultSimulation(Integer memberNo){
        return new SimulEndDto(
                memberAssetRepository.findById(memberNo).get(),
                memberStockRepository.findTop1ByMemberNoAndMemberStockAmountOrderByMemberStockAccEarnPriceAsc(memberNo, 0L),
                memberStockRepository.findTop1ByMemberNoAndMemberStockAmountOrderByMemberStockAccEarnPriceDesc(memberNo, 0L)
                );
    }

}
