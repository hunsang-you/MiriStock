package com.udteam.miristock.service;

import com.udteam.miristock.dto.LimitPriceOrderDto;
import com.udteam.miristock.dto.MemberAssetDto;
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
    private final MemberAssetRepository memberAssetRepository;

    public List<LimitPriceOrderDto> findAll(Integer memberNo, Deal limitPriceOrderType) {
        List<LimitPriceOrderEntity> limitPriceOrderEntityList = limitPriceOrderCustomRepository.findAllByMemberNoAndLimitPriceOrderType(memberNo, limitPriceOrderType);
        return limitPriceOrderEntityList.stream()
                .map(LimitPriceOrderDto::new)
                .collect(Collectors.toList());
    }

    public List<LimitPriceOrderDto> getLimitPriceOrderAllList(Integer memberNo) {
        List<LimitPriceOrderEntity> limitPriceOrderEntityList = limitPriceOrderRepository.findAllByMemberNo(memberNo);
        return limitPriceOrderEntityList.stream()
                .map(LimitPriceOrderDto::new)
                .collect(Collectors.toList());
    }

    // 단건 주식 매수/매도 로직
    @Transactional
    public Object oneLimitPriceOrderSave(LimitPriceOrderDto limitPriceOrderDto, MemberAssetDto memberAssetDto) {
        Integer memberSimulationTime = memberAssetDto.getMemberassetCurrentTime();

        // 해당 종목의 시뮬레이션 시간때의 종가를 들고온다.
        StockDataResponseDto getStockData =
                new StockDataResponseDto(stockDataRepository.findByStockCodeAndStockDataDate(limitPriceOrderDto.getStockCode(), memberSimulationTime));
        Long limitPriceOrderClosingPrice = limitPriceOrderDto.getLimitPriceOrderPrice(); // 매수/매도 예약가
        Deal limitPriceOrderType = limitPriceOrderDto.getLimitPriceOrderType(); // 매수/매도 타입 구분
        Long getClosingPriceOnTime = getStockData.getStockDataClosingPrice(); // 해당 날짜 종가

        Object result = null;

        // 매수 요청 ============================
        if (limitPriceOrderType == Deal.BUY) {
            if (limitPriceOrderClosingPrice >= getClosingPriceOnTime) { // 매수할 금액이 종가보다 크다면 구매.
                // 거래내역에 추가한다.
                result = stockDealRepository.save(StockDealEntity.builder()
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
                            .memberStockAccPurchasePrice(limitPriceOrderDto.getLimitPriceOrderPrice() * limitPriceOrderDto.getLimitPriceOrderAmount())
                            .memberStockAccSellPrice(0L)
                            .memberStockAccEarnRate(0.0f)
                            .build()
                    );

                } else { // 만약 보유주식에 동일 종목이 있다면 업데이트 해야함. (이전에 샀던것들..)

                    // 현재 보유량 + 추가 매수량
                    Long sumAmount = getMemberStockCode.getMemberStockAmount() + limitPriceOrderDto.getLimitPriceOrderAmount();
                    // 총 구입 가격 (누적용) -> 기존 총 구입량 + 현재 주문량 X 요청가격
                    Long totalPurchasePrice = getMemberStockCode.getMemberStockAccPurchasePrice() +
                            (long)limitPriceOrderDto.getLimitPriceOrderPrice() * limitPriceOrderDto.getLimitPriceOrderAmount();
                    // 평단가 계산
                    Long avgPrice = (getMemberStockCode.getMemberStockAmount() * getMemberStockCode.getMemberStockAvgPrice() +
                            limitPriceOrderDto.getLimitPriceOrderAmount() * limitPriceOrderDto.getLimitPriceOrderPrice() ) / sumAmount;

                    // 보유 주식 보유량, 평균매수가 업데이트 하기 (구매)
                    memberStockRepository.save(MemberStockEntity.builder()
                            .memberStockNo(getMemberStockCode.getMemberStockNo())
                            .stockCode(getMemberStockCode.getStockCode())
                            .stockName(getMemberStockCode.getStockName())
                            .memberNo(getMemberStockCode.getMemberNo())
                            .memberStockAmount(sumAmount)
                            .memberStockAvgPrice(avgPrice)
                            .memberStockAccPurchasePrice(totalPurchasePrice)
                            .memberStockAccSellPrice(getMemberStockCode.getMemberStockAccSellPrice())
                            .memberStockAccEarnRate(getMemberStockCode.getMemberStockAccEarnRate())
                            .build()
                    );
                }

                // 자산현황 업데이트
                // 자산현황 들고오기
                MemberAssetEntity getMemberAsset = memberAssetRepository.findByMember_MemberNo(limitPriceOrderDto.getMemberNo());
//                log.info("매수 하고 나서 자산현황 업뎃");
//                log.info("getMemberStockCode.getMemberStockAvgPrice() :{}",getMemberStockCode.getMemberStockAvgPrice());
//                log.info("getMemberAsset.getMemberassetAvailableAsset() : {}", getMemberAsset.getMemberassetAvailableAsset());
//                log.info("getMemberAsset.getMemberassetStockAsset(): {}" , getMemberAsset.getMemberassetStockAsset());
//                log.info("limitPriceOrderDto.getLimitPriceOrderAmount(); : {}", limitPriceOrderDto.getLimitPriceOrderAmount());
//                log.info("limitPriceOrderDto.getLimitPriceOrderPrice() : {}", limitPriceOrderDto.getLimitPriceOrderPrice());

                Long availableAsset = getMemberAsset.getMemberassetAvailableAsset()
                        - limitPriceOrderDto.getLimitPriceOrderPrice() * limitPriceOrderDto.getLimitPriceOrderAmount();
                Long stockAsset = getMemberAsset.getMemberassetStockAsset()
                        + limitPriceOrderDto.getLimitPriceOrderPrice() * limitPriceOrderDto.getLimitPriceOrderAmount();
                memberAssetRepository.save(MemberAssetEntity.builder()
                        .memberassetNo(getMemberAsset.getMemberassetNo())
                        .member(MemberEntity.builder().memberNo(limitPriceOrderDto.getMemberNo()).build())
                        .memberassetCurrentTime(memberSimulationTime)
                        .memberassetTotalAsset(availableAsset + stockAsset)
                        .memberassetAvailableAsset(availableAsset)
                        .memberassetStockAsset(stockAsset)
                        .memberassetLastTotalAsset(getMemberAsset.getMemberassetLastTotalAsset())
                        .build());

                // 추가하면서 임시리스트 목록 기반하여 매수 예정 내역 db에서 지운다.
                if(limitPriceOrderDto.getLimitPriceOrderNo() != null){
                    limitPriceOrderRepository.deleteAllByMemberNoAndLimitPriceOrderNo(limitPriceOrderDto.getMemberNo(), limitPriceOrderDto.getLimitPriceOrderNo());
                }

                log.info("매수 요청 완료");

            } else {  // 매수할 금액이 종가보다 작다면 예약 목록에 등록합니다.
                limitPriceOrderRepository.saveAndFlush(limitPriceOrderDto.toEntity());
//                result = new LimitPriceOrderDto(limitPriceOrderRepository.saveAndFlush(limitPriceOrderDto.toEntity()));
                log.info("매수 예약 등록됨");
                result = limitPriceOrderRepository.saveAndFlush(limitPriceOrderDto.toEntity());
            }
            // 매수 요청 끝 ============================

            // 매도 요청 ======================
        } else if (limitPriceOrderType == Deal.SELL) {
            if (limitPriceOrderClosingPrice <= getClosingPriceOnTime) { // 매도할 금액이 종가보다 작다면 판매합니다.

                // 보유 주식의 평균가 들고와야함.
                MemberStockEntity getMemberStockCode = memberStockRepository.findByMemberNoAndStockCode(limitPriceOrderDto.getMemberNo(), limitPriceOrderDto.getStockCode());
                if (getMemberStockCode.getMemberStockAmount() < limitPriceOrderDto.getLimitPriceOrderAmount()){
                    log.info("보유주식보다 더 많은 매도량 요청됨 (승인거절)");
                    return "보유주식보다 더 많은 매도량 요청됨 (승인거절)";
                }

                // 보유 주식의 평균가 기반으로 판매했을 때 이익 계산해야함.
                // 거래내역에 추가 ==================
                Long earnPrice = (limitPriceOrderDto.getLimitPriceOrderPrice() - getMemberStockCode.getMemberStockAvgPrice()) * limitPriceOrderDto.getLimitPriceOrderAmount();
                Float earnRate = ((float) getMemberStockCode.getMemberStockAvgPrice() / (float) limitPriceOrderDto.getLimitPriceOrderPrice()) * (float) 100 - (float) 100;
                result = stockDealRepository.save(StockDealEntity.builder()
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
                Long sellPrice = limitPriceOrderClosingPrice * limitPriceOrderDto.getLimitPriceOrderAmount();
//                Long sellPrice = (getMemberStockCode.getMemberStockAvgPrice() - limitPriceOrderClosingPrice) * sumAmount;
                Long totalSellPrice = sellPrice + getMemberStockCode.getMemberStockAccSellPrice();
                Long totalPurchasePrice = getMemberStockCode.getMemberStockAccPurchasePrice();
                Float earnRate2 = (float)(totalSellPrice - totalPurchasePrice) / (float)totalPurchasePrice * 100f;

                // 보유 주식 보유량, 평균매수가 업데이트 + 해당 종목에 대한 누적수익률, 수익금 계산
                memberStockRepository.save(MemberStockEntity.builder()
                        .memberStockNo(getMemberStockCode.getMemberStockNo())
                        .stockCode(getMemberStockCode.getStockCode())
                        .stockName(getMemberStockCode.getStockName())
                        .memberNo(getMemberStockCode.getMemberNo())
                        .memberStockAmount(sumAmount)
                        .memberStockAvgPrice(getMemberStockCode.getMemberStockAvgPrice())
                        .memberStockAccPurchasePrice(getMemberStockCode.getMemberStockAccPurchasePrice())
                        .memberStockAccSellPrice(totalSellPrice)
                        .memberStockAccEarnRate(earnRate2)
                        .build()
                );
                // 자산현황 업데이트
                // 자산현황 들고오기
                MemberAssetEntity getMemberAsset = memberAssetRepository.findByMember_MemberNo(limitPriceOrderDto.getMemberNo());
//                log.info("매도 하고 나서 자산현황 업뎃");
//                log.info("getMemberStockCode.getMemberStockAvgPrice() :{}",getMemberStockCode.getMemberStockAvgPrice());
//                log.info("getMemberAsset.getMemberassetAvailableAsset() : {}", getMemberAsset.getMemberassetAvailableAsset());
//                log.info("getMemberAsset.getMemberassetStockAsset(): {}" , getMemberAsset.getMemberassetStockAsset());
//                log.info("limitPriceOrderDto.getLimitPriceOrderAmount(); : {}", limitPriceOrderDto.getLimitPriceOrderAmount());
//                log.info("limitPriceOrderDto.getLimitPriceOrderPrice() : {}", limitPriceOrderDto.getLimitPriceOrderPrice());

                // 현금자산
                Long availableAsset = getMemberAsset.getMemberassetAvailableAsset()
                        + ((limitPriceOrderDto.getLimitPriceOrderPrice() - getMemberStockCode.getMemberStockAvgPrice()) * limitPriceOrderDto.getLimitPriceOrderAmount());
                Long stockAsset = getMemberAsset.getMemberassetStockAsset()
                        - getMemberStockCode.getMemberStockAvgPrice() * limitPriceOrderDto.getLimitPriceOrderAmount();
                memberAssetRepository.save(MemberAssetEntity.builder()
                        .memberassetNo(limitPriceOrderDto.getMemberNo())
                        .member(MemberEntity.builder().memberNo(limitPriceOrderDto.getMemberNo()).build())
                        .memberassetCurrentTime(memberSimulationTime)
                        .memberassetTotalAsset(availableAsset + stockAsset)
                        .memberassetAvailableAsset(availableAsset)
                        .memberassetStockAsset(stockAsset)
                        .memberassetLastTotalAsset(getMemberAsset.getMemberassetLastTotalAsset())
                        .build());

                // 추가하면서 임시리스트 목록 기반하여 매수 예정 내역 db에서 지운다.
                if(limitPriceOrderDto.getLimitPriceOrderNo() != null){
                    limitPriceOrderRepository.deleteAllByMemberNoAndLimitPriceOrderNo(limitPriceOrderDto.getMemberNo(), limitPriceOrderDto.getLimitPriceOrderNo());
                }

                log.info("매도 요청 완료");

            } else {  // 매도할 금액이 종가보다 크면 예약 목록에 등록합니다.
                //result = new LimitPriceOrderDto(limitPriceOrderRepository.saveAndFlush(limitPriceOrderDto.toEntity()));
                log.info("매도 예약 목록 등록");
                result = limitPriceOrderRepository.saveAndFlush(limitPriceOrderDto.toEntity());
            }
            // 매도 요청 끝 ======================
        }
        return result;
    }

    @Transactional
    public Object oneLimitPriceOrderUpdate(LimitPriceOrderDto limitPriceOrderDto, MemberAssetDto memberAssetDto) {
        // 거래예정 내역 수정
        LimitPriceOrderDto updateLimitPriceOrder = new LimitPriceOrderDto(limitPriceOrderRepository.saveAndFlush(limitPriceOrderDto.toEntity()));
        return oneLimitPriceOrderSave(updateLimitPriceOrder, memberAssetDto);
    }

    @Transactional
    public int delete(Integer memberNo, Integer limitPriceOrderNo) {
        return limitPriceOrderRepository.deleteAllByMemberNoAndLimitPriceOrderNo(memberNo, limitPriceOrderNo);
    }
}
