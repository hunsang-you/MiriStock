package com.udteam.miristock.service;

import com.udteam.miristock.dto.ArticleRequestDto;
import com.udteam.miristock.dto.ArticleResponseDto;
import com.udteam.miristock.dto.LimitPriceOrderDto;
import com.udteam.miristock.dto.StockDataResponseDto;
import com.udteam.miristock.entity.*;
import com.udteam.miristock.repository.*;
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
    private final StockDataRepository stockDataRepository;
    private final StockDealRepository stockDealRepository;
    private final MemberStockRepository memberStockRepository;

    public List<LimitPriceOrderDto> findAll(Integer memberNo, Deal limitPriceOrderType) {

        List<LimitPriceOrderEntity> limitPriceOrderEntityList = limitPriceOrderCustomRepository.findAllByMemberNoAndLimitPriceOrderType(memberNo, limitPriceOrderType);
        return limitPriceOrderEntityList.stream()
                .map(LimitPriceOrderDto::new)
                .collect(Collectors.toList());
    }

    // 단건 주식 매수/매도 로직
    @Transactional
    public LimitPriceOrderDto oneLimitPriceOrderSave(LimitPriceOrderDto limitPriceOrderDto, Integer memberSimulationTime) {

        // 해당 종목의 시뮬레이션 시간때의 종가를 들고온다.
        StockDataResponseDto getStockData =
                new StockDataResponseDto(stockDataRepository.findByStockCodeAndStockDataDate(limitPriceOrderDto.getStockCode(), memberSimulationTime));
        Long limitPriceOrderClosingPrice = limitPriceOrderDto.getLimitPriceOrderPrice(); // 매수/매도 예약가
        Long getClosingPriceOnTime = getStockData.getStockDataClosingPrice(); // 해당 날짜 종가
        Deal limitPriceOrderType = limitPriceOrderDto.getLimitPriceOrderType(); // 매수/매도 타입 구분

        LimitPriceOrderDto result = null;

        // 매수 타입일 때
        if (limitPriceOrderType == Deal.BUY) {
            if (limitPriceOrderClosingPrice >= getClosingPriceOnTime) { // 매수할 금액이 종가보다 크다면 구매.
                // 거래내역에 추가한다.
                stockDealRepository.save(StockDealEntity.builder()
                        .stockCode(limitPriceOrderDto.getStockCode())
                        .stockName(limitPriceOrderDto.getStockName())
                        .memberNo(limitPriceOrderDto.getMemberNo())
                        .stockDealDate(memberSimulationTime)
                        .stockDealOrderClosingPrice(limitPriceOrderDto.getLimitPriceOrderPrice())
                        .stockDealAmount(limitPriceOrderDto.getLimitPriceOrderAmount())
                        .stockDealType(limitPriceOrderType)
                        .build());

                // 보유 주식 목록에 추가한다.
                // 보유 주식 목록에 동일 종목이 있는지 체크
                MemberStockEntity getMemberStockCode = memberStockRepository.findByMemberNoAndStockCode(limitPriceOrderDto.getMemberNo(), limitPriceOrderDto.getStockCode());
                // 만약 보유주식에 동일 종목이 없다면 그대로 추가..
                if (getMemberStockCode == null) {
                    memberStockRepository.save(MemberStockEntity.builder()
                            .stockCode(limitPriceOrderDto.getStockCode())
                            .stockName(limitPriceOrderDto.getStockName())
                            .memberNo(limitPriceOrderDto.getMemberNo())
                            .memberStockAmount(limitPriceOrderDto.getLimitPriceOrderAmount())
                            .memberStockAvgPrice(limitPriceOrderDto.getLimitPriceOrderPrice())
                            .build()
                    );
                } else { // 만약 보유주식에 동일 종목이 있다면 업데이트 해야함.
                    Long sumAmount = getMemberStockCode.getMemberStockAmount() + limitPriceOrderDto.getLimitPriceOrderAmount();
                    Long totalPurchasePrice =
                            (long)limitPriceOrderDto.getLimitPriceOrderPrice() * limitPriceOrderDto.getLimitPriceOrderAmount() +
                                    (long)getMemberStockCode.getMemberStockAvgPrice() * getMemberStockCode.getMemberStockAmount();
                    // 보유 주식 보유량, 평균매수가 업데이트 하기
                    memberStockRepository.save(MemberStockEntity.builder()
                            .memberStockNo(getMemberStockCode.getMemberStockNo())
                            .stockCode(getMemberStockCode.getStockCode())
                            .stockName(getMemberStockCode.getStockName())
                            .memberNo(getMemberStockCode.getMemberNo())
                            .memberStockAmount(sumAmount)
                            .memberStockAvgPrice((totalPurchasePrice / sumAmount))
                            .build()
                    );
                }
            } else {  // 매수할 금액이 종가보다 작다면 예약 목록에 등록합니다.
                result = new LimitPriceOrderDto(limitPriceOrderRepository.saveAndFlush(limitPriceOrderDto.toEntity()));
            }
            // 매도 타입일 때
        } else if (limitPriceOrderType == Deal.SELL) {
            if (limitPriceOrderClosingPrice <= getClosingPriceOnTime) { // 매도할 금액이 종가보다 작다면 판매합니다.

                // 보유 주식의 평균가 들고와야함.
                MemberStockEntity getMemberStockCode = memberStockRepository.findByMemberNoAndStockCode(limitPriceOrderDto.getMemberNo(), limitPriceOrderDto.getStockCode());

                // 보유 주식의 평균가 기반으로 판매했을 때 이익 계산해야함.
                // 거래내역에 추가 ==================
                Long earnPrice = (limitPriceOrderDto.getLimitPriceOrderPrice() - getMemberStockCode.getMemberStockAvgPrice()) * limitPriceOrderDto.getLimitPriceOrderAmount();
                Float earnRate = ((float) getMemberStockCode.getMemberStockAvgPrice() / (float) limitPriceOrderDto.getLimitPriceOrderPrice()) * (float) 100 - (float) 100;
                stockDealRepository.save(StockDealEntity.builder()
                        .stockCode(limitPriceOrderDto.getStockCode())
                        .stockName(limitPriceOrderDto.getStockName())
                        .memberNo(limitPriceOrderDto.getMemberNo())
                        .stockDealDate(memberSimulationTime)
                        .stockDealOrderClosingPrice(limitPriceOrderDto.getLimitPriceOrderPrice()) // 매도가
                        .stockDealAvgClosingPrice(getMemberStockCode.getMemberStockAvgPrice()) // 평균가
                        .stockDealAmount(limitPriceOrderDto.getLimitPriceOrderAmount())
                        .stockDealType(limitPriceOrderType)
                        .stockDealEarnRate(earnRate)
                        .stockDealEarnPrice(earnPrice)
                        .build());
                // 거래내역에 추가 =================

                // 보유 주식 목록에 업데이트 한다.
                Long sumAmount = getMemberStockCode.getMemberStockAmount() - limitPriceOrderDto.getLimitPriceOrderAmount();

                Long totalSellPrice = (getMemberStockCode.getMemberStockAvgPrice() - limitPriceOrderClosingPrice) * sumAmount;


                // 보유 주식 보유량, 평균매수가 업데이트 + 해당 종목에 대한 누적수익률, 수익금 계산
//                memberStockRepository.save(MemberStockEntity.builder()
//                                .memberStockNo(getMemberStockCode.getMemberStockNo())
//                                .stockCode(getMemberStockCode.getStockCode())
//                                .stockName(getMemberStockCode.getStockName())
//                                .memberNo(getMemberStockCode.getMemberNo())
//                                .memberStockAmount(sumAmount)
//                                .memberStockAvgPrice(getMemberStockCode.getMemberStockAvgPrice())
//                                .memberStockAccEarnRate()
//                                .memberStockAccEarnPrice(getMemberStockCode.getMemberStockAccEarnPrice() + totalSellPrice)
//                                .build()
//                );


            } else {  // 매도할 금액이 종가보다 크면 예약 목록에 등록합니다.
                result = new LimitPriceOrderDto(limitPriceOrderRepository.saveAndFlush(limitPriceOrderDto.toEntity()));
            }

        }

        return result;
    }

//    @Transactional
//    public void delete(Integer memberNo, Integer limitPriceOrderNo) {
//        limitPriceOrderRepository.deleteAllByMemberNoAndLimitPriceOrderNo(memberNo, limitPriceOrderNo);
//    }
}
