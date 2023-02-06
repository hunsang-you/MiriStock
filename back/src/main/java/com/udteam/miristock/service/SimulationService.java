package com.udteam.miristock.service;

import com.udteam.miristock.dto.SimulEndDto;
import com.udteam.miristock.repository.MemberAssetRepository;
import com.udteam.miristock.repository.MemberStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulationService {

    private final MemberAssetRepository memberAssetRepository;
    private final MemberStockRepository memberStockRepository;

    @Transactional(readOnly = true)
    public SimulEndDto resultSimulation(Integer memberNo){
        return new SimulEndDto(
                memberAssetRepository.findById(memberNo).get(),
                memberStockRepository.findTop1ByMemberNoAndMemberStockAmountOrderByMemberStockAccEarnPriceAsc(memberNo, 0L),
                memberStockRepository.findTop1ByMemberNoAndMemberStockAmountOrderByMemberStockAccEarnPriceDesc(memberNo, 0L)
                );
    }


}
