package com.udteam.miristock.service;

import com.udteam.miristock.config.ValueConfig;
import com.udteam.miristock.dto.*;
import com.udteam.miristock.entity.*;
import com.udteam.miristock.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulationService {
    private final MemberAssetRepository memberAssetRepository;
    private final MemberStockRepository memberStockRepository;
    private final LimitPriceOrderRepository limitPriceOrderRepository;
    private final StockDealRepository stockDealRepository;

    @Transactional
    public SimulEndDto resultSimulation(Integer memberNo){
        // 회원자산 불러오기
        MemberAssetEntity memberAssetEntity = memberAssetRepository.findById(memberNo).get();

        // 매수 예정 내역들 전부 취소하기(삭제)
        limitPriceOrderRepository.deleteAllByMemberNoAndLimitPriceOrderType(memberNo, Deal.BUY);
        
        // 매도 예정 내역들 불러오기
        List<Object[]> limitPriceOrderListSELL = limitPriceOrderRepository.compareLimitPriceOrderWithTodayStockData(memberNo, memberAssetEntity.getMemberassetCurrentTime(), Deal.SELL);
        // 현재가로 팔아버리기
        
        Long totalMemberStockPriceSum = 0L; // 처음 주식 구입 금액 합
        Long curStockPriceSum = 0L; // 팔때 주식 금액 합

        for (Object[] objects : limitPriceOrderListSELL) {
            MemberStockEntity memberStockEntity = (MemberStockEntity) objects[0];
            StockDataEntity stockDataEntity = (StockDataEntity) objects[1];
            Long curStockAmount = stockDataEntity.getStockDataAmount();
            String targetStockCode = memberStockEntity.getStockCode();

            Long memStockPrice = memberStockEntity.getMemberStockAvgPrice() * curStockAmount;
            Long curStockPrice = stockDataEntity.getStockDataClosingPrice() * curStockAmount;

            totalMemberStockPriceSum += memStockPrice;
            curStockPriceSum += curStockPrice;

            // 해당 주식 목록의 수익률, 수익금도 반영해야함...
            // 회원 보유 주식 업데이트
            memberStockRepository.save(MemberStockEntity.builder()
                    .memberStockNo(memberStockEntity.getMemberStockNo())
                    .stockCode(memberStockEntity.getStockCode())
                    .stockName(memberStockEntity.getStockName())
                    .memberNo(memberStockEntity.getMemberNo())
                    .memberStockAmount(0L)
                    .memberStockAvgPrice(0L)
                    .memberStockAccPurchasePrice(memberStockEntity.getMemberStockAccPurchasePrice())
                    .memberStockAccSellPrice(memberStockEntity.getMemberStockAccSellPrice() + curStockPriceSum)
                    .memberStockAccEarnRate(
                            memberStockEntity.getMemberStockAccPurchasePrice()
                            / (memberStockEntity.getMemberStockAccSellPrice() + curStockPriceSum)
                                    * (float)100 - (float)100)
                    .build()
            );

        }

        // 매도 예정 내역들 전부 취소하기(삭제)
        limitPriceOrderRepository.deleteAllByMemberNoAndLimitPriceOrderType(memberNo, Deal.SELL);

        // 회원 자산 업데이트
        memberAssetRepository.save(MemberAssetEntity.builder()
                        .memberassetNo(memberAssetEntity.getMemberassetNo())
                        .member(memberAssetEntity.getMember())
                        .memberassetTotalAsset(memberAssetEntity.getMemberassetTotalAsset() + curStockPriceSum)
                        .memberassetAvailableAsset(memberAssetEntity.getMemberassetAvailableAsset() +curStockPriceSum)
                        .memberassetStockAsset(memberAssetEntity.getMemberassetStockAsset() - totalMemberStockPriceSum)
                        .memberassetLastTotalAsset(memberAssetEntity.getMemberassetLastTotalAsset())
                        .build());

        // 결과 데이터 출력
        return new SimulEndDto(
                memberAssetEntity,
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
                        .memberassetLastTotalAsset(ValueConfig.memberInitAvailableAsset)
                        .build());

        // 거래예정 테이블 삭제
        limitPriceOrderRepository.deleteById(memberDto.getMemberNo());

        // 회원 보유 주식 테이블 삭제
        memberStockRepository.deleteById(memberDto.getMemberNo());

        // 회원 주식 거래 내역 테이블 삭제
        stockDealRepository.deleteById(memberDto.getMemberNo());

    }

}
