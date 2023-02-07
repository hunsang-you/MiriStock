package com.udteam.miristock.service;

import com.udteam.miristock.dto.*;
import com.udteam.miristock.entity.Deal;
import com.udteam.miristock.entity.LimitPriceOrderEntity;
import com.udteam.miristock.entity.StockDataEntity;
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
    private final StockDataRepository stockDataRepository;
    private final LimitPriceOrderRepository limitPriceOrderRepository;

    @Transactional(readOnly = true)
    public SimulEndDto resultSimulation(Integer memberNo){
        return new SimulEndDto(
                memberAssetRepository.findById(memberNo).get(),
                memberStockRepository.findTop1ByMemberNoAndMemberStockAmountOrderByMemberStockAccEarnPriceAsc(memberNo, 0L),
                memberStockRepository.findTop1ByMemberNoAndMemberStockAmountOrderByMemberStockAccEarnPriceDesc(memberNo, 0L)
                );
    }

    @Transactional
    public void updateSimulationDate(Integer memberNo, Integer simulationDate){

        Integer memberDate = memberAssetRepository.findById(memberNo).get().getMemberassetCurrentTime();
        log.info("회원의 시뮬레이션 시간 : {}", memberDate);
        String memberDateStr = null;

        // 날짜 먼저 더하기
        while(true) {
            memberDateStr = AddDate(String.valueOf(memberDate), 0, 0, simulationDate);
            // 해당 날짜 데이터가 있는지 확인하기
            memberDate = Integer.parseInt(memberDateStr);
            StockDataInfoMapping result = stockDataRepository.findTop1ByStockDataDate(memberDate);
            if (result == null) {
                log.info("날짜 데이터 체크 -> null 입니다.");
            } else {
                log.info("날짜 데이터 체크 : {}", result.getStockDataDate());
                break;
            }
        }

        // 회원 매수 매도 예약 리스트 종목 들고오기
        List<Object[]> result = limitPriceOrderRepository.compareLimitPriceOrderWithTodayStockData(memberNo, memberDate);
        // 매수 예약 내역
        List<LimitPriceOrderCompareStockDeal> LimitPriceOrderBuyCompareStockList = new ArrayList<>();
        // 매도 예약 내역
        List<LimitPriceOrderCompareStockDeal> LimitPriceOrderSellCompareStockDealList = new ArrayList<>();

//        for(Object[] arr : result){
//            LimitPriceOrderBuyCompareStockList.add(
//                    new LimitPriceOrderCompareStockDeal((StockDataEntity) Arrays.asList(arr).get(0), (LimitPriceOrderEntity) Arrays.asList(arr).get(1)));
//        }

        // 회원 매수 매도할 내역들 들고와서 거래내역에 넣기
        for (int i = 0; i < result.size(); i++) {
            LimitPriceOrderCompareStockDeal data = new LimitPriceOrderCompareStockDeal(
                    (StockDataEntity) Arrays.asList(result.get(i)).get(0), (LimitPriceOrderEntity) Arrays.asList(result.get(i)).get(1) );
            // 매수할 임시 리스트
            if( data.getLimitPriceOrderType() == Deal.BUY ){
                // 사려는 금액보다 종가가 낮아지면 구매 리스트에 넣기
                if(data.getStockDataClosingPrice() <= data.getLimitPriceOrderPrice()){
                    LimitPriceOrderBuyCompareStockList.add(data);
                }
            }
            // 매도 테이블 등록
            else if(data.getLimitPriceOrderType() == Deal.SELL) {
                // 팔려는 금액보다 종가가 높아지면 판매 리스트에 넣기
                if(data.getStockDataClosingPrice() >= data.getLimitPriceOrderPrice()){
                    LimitPriceOrderSellCompareStockDealList.add(data);
                }
            }
        }

        //



        //


        // 사려는 금액

//        System.out.println(LimitPriceOrderCompareStockDealList.get(0).getStockCode());
//        System.out.println(LimitPriceOrderCompareStockDealList.get(1).getStockCode());

        // 주식 데이터 종목과 종가 비교
        // 판매 주식 팔아버리기



//         거래가와 거래수량이 맞으면
//
//         거래리스트에 넣고
//
//         보유 주식 목록에 넣음 (거래가 전부 완료된 건-> 보유량 0인것  누적 수익률, 누적 금액 데이터 반영)
//
//         보유 주식 목록 가져와서 회원자산에 반영



    }


}
