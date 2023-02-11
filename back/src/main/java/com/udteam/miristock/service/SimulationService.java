package com.udteam.miristock.service;

import com.udteam.miristock.config.ValueConfig;
import com.udteam.miristock.dto.*;
import com.udteam.miristock.entity.*;
import com.udteam.miristock.repository.*;
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
    private final LimitPriceOrderRepository limitPriceOrderRepository;

    private final StockDealRepository stockDealRepository;

    @Transactional(readOnly = true)
    public SimulEndDto resultSimulation(Integer memberNo){
        return new SimulEndDto(
                memberAssetRepository.findById(memberNo).get(),
                new MemberSimulEndDto(memberStockRepository.findTop1ByMemberNoAndMemberStockAmountOrderByMemberStockAccEarnPriceDesc(memberNo, 0L).get(0)),
                new MemberSimulEndDto(memberStockRepository.findTop1ByMemberNoAndMemberStockAmountOrderByMemberStockAccEarnPriceAsc(memberNo, 0L).get(0))
                );
    }

    // 시뮬레이션 종료...
    @Transactional
    public void resetSimulation(MemberDto memberDto){
        // 회원 자산 기본으로 초기화
        MemberAssetEntity memberAssetResetResult  = memberAssetRepository.save(MemberAssetEntity.builder()
                        .memberassetNo(memberDto.getMemberNo())
                        .member(MemberEntity.builder().memberNo(memberDto.getMemberNo()).build())
                        .memberassetAvailableAsset(ValueConfig.memberInitAvailableAsset)
                        .memberassetStockAsset(0L)
                        .memberassetTotalAsset(ValueConfig.memberInitAvailableAsset)
                        .memberassetCurrentTime(ValueConfig.memberInitSimulationTime)
                        .build());

        // 거래예정 테이블 삭제
        limitPriceOrderRepository.deleteById(memberDto.getMemberNo());

        // 회원 보유 주식 테이블 삭제
        memberStockRepository.deleteById(memberDto.getMemberNo());

        // 회원 주식 거래 내역 테이블 삭제
        stockDealRepository.deleteById(memberDto.getMemberNo());

    }

}
