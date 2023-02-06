package com.udteam.miristock.service;

import com.udteam.miristock.dto.LimitPriceOrderDto;
import com.udteam.miristock.dto.SimulEndDto;
import com.udteam.miristock.dto.StockDataInfoMapping;
import com.udteam.miristock.entity.LimitPriceOrderEntity;
import com.udteam.miristock.entity.StockDealEntity;
import com.udteam.miristock.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.udteam.miristock.service.InformationService.AddDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulationService {

    private final MemberAssetRepository memberAssetRepository;
    private final MemberStockRepository memberStockRepository;
    private final StockDataRepository stockDataRepository;
    private final StockDealRepository stockDealRepository;
    private final LimitPriceOrderRepository limitPriceOrderRepository;

    private final LimitPriceOrderService limitPriceOrderService;


    @Transactional(readOnly = true)
    public SimulEndDto resultSimulation(Integer memberNo){
        return new SimulEndDto(
                memberAssetRepository.findById(memberNo).get(),
                memberStockRepository.findTop1ByMemberNoAndMemberStockAmountOrderByMemberStockAccEarnPriceAsc(memberNo, 0L),
                memberStockRepository.findTop1ByMemberNoAndMemberStockAmountOrderByMemberStockAccEarnPriceDesc(memberNo, 0L)
                );
    }

//    @Transactional
//    public void updateSimulationDate(Integer memberNo, Integer simulationDate){
//
//        Integer memberDate = memberAssetRepository.findById(memberNo).get().getMemberassetCurrentTime();
//        log.info("회원의 시뮬레이션 시간 : {}", memberDate);
//        String memberDateStr = null;
//
//        // 날짜 먼저 더하기
//        while(true) {
//            memberDateStr = AddDate(String.valueOf(memberDate), 0, 0, simulationDate);
//            // 해당 날짜 데이터가 있는지 확인하기
//            memberDate = Integer.parseInt(memberDateStr);
//            StockDataInfoMapping result = stockDataRepository.findTop1ByStockDataDate(memberDate);
//            if (result == null) {
//                log.info("날짜 데이터 체크 -> null 입니다.");
//            } else {
//                log.info("날짜 데이터 체크 : {}", result.getStockDataDate());
//                break;
//            }
//        }
//
//        // 회원 매수 매도 예약 리스트 종목 들고오기
//        List<LimitPriceOrderDto> limitPriceOrderEntityList = limitPriceOrderService.findAll(memberNo);




        // 주식 데이터 종목과 종가 비교

        // 거래가와 거래수량이 맞으면

        // 거래리스트에 넣고

        // 보유 주식 목록에 넣음 (거래가 전부 완료된 건-> 보유량 0인것  누적 수익률, 누적 금액 데이터 반영)

        // 보유 주식 목록 가져와서 회원자산에 반영



//    }


}
