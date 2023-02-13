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
        // 멤버 자산 불러오기
        MemberAssetEntity getMemberAssetEntity = memberAssetRepository.findByMember_MemberNo(memberAssetDto.getMemberNo());

        // 해당 종목의 시뮬레이션 시간때의 종가를 들고온다.
        StockDataResponseDto getStockData =
                new StockDataResponseDto(stockDataRepository.findByStockCodeAndStockDataDate(limitPriceOrderDto.getStockCode(), memberSimulationTime));

        Long limitPriceOrderClosingPrice = limitPriceOrderDto.getLimitPriceOrderPrice(); // 매수/매도 예약가
        Deal limitPriceOrderType = limitPriceOrderDto.getLimitPriceOrderType(); // 매수/매도 타입 구분
        Long getClosingPriceOnTime = getStockData.getStockDataClosingPrice(); // 해당 날짜 종가

        Object result = null;

        // 매수 요청 ============================
        if (limitPriceOrderType == Deal.BUY) {
            // 회원 자산 금액이 사려는 금액보다 적으면 리턴합니다.
            if(getMemberAssetEntity.getMemberassetAvailableAsset() <
                    (long) Math.floor(((double)limitPriceOrderDto.getLimitPriceOrderPrice() * 1.005) * (double)limitPriceOrderDto.getLimitPriceOrderAmount())){
                return "회원 현금 자산이 구매하려는 주식금액보다 적어서 구매할 수 없습니다.";
            }

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
                MemberStockEntity memberStockEntity = memberStockRepository.findByMemberNoAndStockCode(limitPriceOrderDto.getMemberNo(), limitPriceOrderDto.getStockCode());
                // 만약 보유주식에 동일 종목이 없다면 그대로 추가..
                if (memberStockEntity == null) {
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
                    Long sumAmount = memberStockEntity.getMemberStockAmount() + limitPriceOrderDto.getLimitPriceOrderAmount();
                    log.info("매수 sumAmount : {}",  sumAmount);
                    // 총 구입 가격 (누적용) -> 기존 총 구입량 + 현재 주문량 X 요청가격
                    Long totalPurchasePrice = memberStockEntity.getMemberStockAccPurchasePrice() +
                            (long)limitPriceOrderDto.getLimitPriceOrderPrice() * limitPriceOrderDto.getLimitPriceOrderAmount();

                    // 평단가 계산
                    Long avgPrice = (memberStockEntity.getMemberStockAmount() * memberStockEntity.getMemberStockAvgPrice() +
                            limitPriceOrderDto.getLimitPriceOrderAmount() * limitPriceOrderDto.getLimitPriceOrderPrice() ) / sumAmount;

                    // 보유 주식 보유량, 평균매수가 업데이트 하기 (구매)
                    memberStockRepository.save(MemberStockEntity.builder()
                            .memberStockNo(memberStockEntity.getMemberStockNo())
                            .stockCode(memberStockEntity.getStockCode())
                            .stockName(memberStockEntity.getStockName())
                            .memberNo(memberStockEntity.getMemberNo())
                            .memberStockAmount(sumAmount)
                            .memberStockAvgPrice(avgPrice)
                            .memberStockAccPurchasePrice(totalPurchasePrice)
                            .memberStockAccSellPrice(memberStockEntity.getMemberStockAccSellPrice())
                            .memberStockAccEarnRate(memberStockEntity.getMemberStockAccEarnRate())
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

                double availableAssetDouble = (double)getMemberAsset.getMemberassetAvailableAsset()
                        - ((double)limitPriceOrderDto.getLimitPriceOrderPrice() * (double)1.005) * (double)limitPriceOrderDto.getLimitPriceOrderAmount();
                long availableAsset = (long) Math.floor(availableAssetDouble);
                log.info("매수 getMemberAsset.getMemberassetAvailableAsset() : {}", getMemberAsset.getMemberassetAvailableAsset());
                log.info("매수((double)limitPriceOrderDto.getLimitPriceOrderPrice() * (double)1.005) * (double)limitPriceOrderDto.getLimitPriceOrderAmount() : {}",
                        ((double)limitPriceOrderDto.getLimitPriceOrderPrice() * (double)1.005) * (double)limitPriceOrderDto.getLimitPriceOrderAmount());
                log.info("availableAsset : {}", availableAsset);

                Long stockAsset = getMemberAsset.getMemberassetStockAsset()
                        + limitPriceOrderDto.getLimitPriceOrderPrice() * limitPriceOrderDto.getLimitPriceOrderAmount();
                log.info("매수 getMemberAsset.getMemberassetStockAsset() : {}", getMemberAsset.getMemberassetStockAsset());
                log.info("매수 limitPriceOrderDto.getLimitPriceOrderPrice() : {} ", limitPriceOrderDto.getLimitPriceOrderPrice());
                log.info("매수 limitPriceOrderDto.getLimitPriceOrderAmount() : {}", limitPriceOrderDto.getLimitPriceOrderAmount());
                log.info("매수 stockAsset : {}", stockAsset);

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
//                limitPriceOrderRepository.saveAndFlush(limitPriceOrderDto.toEntity());
//                result = new LimitPriceOrderDto(limitPriceOrderRepository.saveAndFlush(limitPriceOrderDto.toEntity()));

                log.info("매수 예약 등록됨");
                result = limitPriceOrderRepository.saveAndFlush(limitPriceOrderDto.toEntity());
                log.info("회원 날짜 변경 : {}", memberSimulationTime);
                MemberAssetEntity getMemberAsset = memberAssetRepository.findByMember_MemberNo(limitPriceOrderDto.getMemberNo());
                memberAssetRepository.save(MemberAssetEntity.builder()
                        .memberassetNo(getMemberAsset.getMemberassetNo())
                        .member(MemberEntity.builder().memberNo(limitPriceOrderDto.getMemberNo()).build())
                        .memberassetCurrentTime(memberSimulationTime)
                        .memberassetTotalAsset(getMemberAsset.getMemberassetTotalAsset())
                        .memberassetAvailableAsset(getMemberAsset.getMemberassetAvailableAsset())
                        .memberassetStockAsset(getMemberAsset.getMemberassetStockAsset())
                        .build());
            }
            // 매수 요청 끝 ============================

            // 매도 요청 ======================
        } else if (limitPriceOrderType == Deal.SELL) {
            if (limitPriceOrderClosingPrice <= getClosingPriceOnTime) { // 매도할 금액이 종가보다 작다면 판매합니다.

                // 보유 주식의 평균가 들고와야함.
                MemberStockEntity getMemberStockCode = memberStockRepository.findByMemberNoAndStockCode(limitPriceOrderDto.getMemberNo(), limitPriceOrderDto.getStockCode());
                log.info("limitpriceOrderDto : {}", limitPriceOrderDto);
                if (getMemberStockCode == null ){
                    log.info("보유주식 없음! 잘못된 요청!");
                    log.info("getMemberstockcode is : {}", getMemberStockCode);
                    return "보유주식 없음! 잘못된 요청!";
                } else if  (getMemberStockCode.getMemberStockAmount() < limitPriceOrderDto.getLimitPriceOrderAmount()){
                    log.info("보유주식보다 더 많은 매도량 요청됨 (승인거절)");
                    log.info("getMemberstockcode is : {}", getMemberStockCode);
                    return "보유주식보다 더 많은 매도량 요청됨 (승인거절)";
                } // 여기서 현금보다 적은데 사려고 하면 거절해야한다.


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

                // 자산현황 업데이트
                // 자산현황 들고오기
                MemberAssetEntity getMemberAsset = memberAssetRepository.findByMember_MemberNo(limitPriceOrderDto.getMemberNo());
                log.info("매도 하기 전 자산현황");
                log.info("getMemberStockCode.getMemberStockAvgPrice() -> 보유주식 평균가 :{}",getMemberStockCode.getMemberStockAvgPrice());
                log.info("getMemberAsset.getMemberassetAvailableAsset() -> 회원 현금 자산: {}", getMemberAsset.getMemberassetAvailableAsset());
                log.info("getMemberAsset.getMemberassetStockAsset(): -> 회원 주식 자산 : {} " , getMemberAsset.getMemberassetStockAsset());
                log.info("limitPriceOrderDto.getLimitPriceOrderAmount() -> 회원 주식 판매량 : {}", limitPriceOrderDto.getLimitPriceOrderAmount());
                log.info("limitPriceOrderDto.getLimitPriceOrderPrice() -> 회원 주식 판매가격 : {}", limitPriceOrderDto.getLimitPriceOrderPrice());

                // 현금자산
                double availableAssetDouble = (double)getMemberAsset.getMemberassetAvailableAsset()
                        + (double)((((double)limitPriceOrderDto.getLimitPriceOrderPrice() * 0.995) * (double)limitPriceOrderDto.getLimitPriceOrderAmount()));
                
                long availableAsset = (long) Math.floor(availableAssetDouble);
                // 주식자산
                log.info("getMemberStockCode.getMemberStockAvgPrice() : {}",getMemberStockCode.getMemberStockAvgPrice());
                log.info("limitPriceOrderDto.getLimitPriceOrderAmount() : {}", limitPriceOrderDto.getLimitPriceOrderAmount());
                log.info("getClosingPriceOnTime : {}", getClosingPriceOnTime);
                // 해당 주식 종가로 팔아야함...
                Long stockAsset = getMemberAsset.getMemberassetStockAsset()
                        - (getClosingPriceOnTime * limitPriceOrderDto.getLimitPriceOrderAmount());
//                Long stockAsset = getMemberAsset.getMemberassetStockAsset()
//                        - (getMemberStockCode.getMemberStockAvgPrice() * limitPriceOrderDto.getLimitPriceOrderAmount());


                log.info("availableAssetDouble : {} ", availableAssetDouble);
                log.info("availableAsset (주식 팔고 얻은 현금 자산 금액 총합) : {}", availableAsset);
                log.info("stockAsset (주식 팔고 남은 주식 자산 금액 총합) : {}", stockAsset);
                log.info("memberassetTotalAsset (거래후 자산보유량) : {}",availableAsset + stockAsset);

                memberAssetRepository.save(MemberAssetEntity.builder()
                        .memberassetNo(limitPriceOrderDto.getMemberNo())
                        .member(MemberEntity.builder().memberNo(limitPriceOrderDto.getMemberNo()).build())
                        .memberassetCurrentTime(memberSimulationTime)
                        .memberassetTotalAsset(availableAsset + stockAsset)
                        .memberassetAvailableAsset(availableAsset)
                        .memberassetStockAsset(stockAsset)
                        .memberassetLastTotalAsset(getMemberAsset.getMemberassetLastTotalAsset())
                        .build());

                // 보유 주식 목록에 업데이트 한다.
                Long sumAmount = getMemberStockCode.getMemberStockAmount() - limitPriceOrderDto.getLimitPriceOrderAmount();
                Long sellPrice = limitPriceOrderClosingPrice * limitPriceOrderDto.getLimitPriceOrderAmount();
//                Long sellPrice = (getMemberStockCode.getMemberStockAvgPrice() - limitPriceOrderClosingPrice) * sumAmount;
                Long totalSellPrice = sellPrice + getMemberStockCode.getMemberStockAccSellPrice();
                Long totalPurchasePrice = getMemberStockCode.getMemberStockAccPurchasePrice();
                Float earnRate2 = (float)(totalSellPrice - totalPurchasePrice) / (float)totalPurchasePrice * 100f;

                // 만약 보유 주식을 전부 매도하였을 경우 평균가 0으로 리셋함.
                Long setAvgPriceDefault = getMemberStockCode.getMemberStockAvgPrice();
                if (sumAmount == 0){
                    setAvgPriceDefault = 0L;
                }
                // 보유 주식 보유량, 평균매수가 업데이트 + 해당 종목에 대한 누적수익률, 수익금 계산
                memberStockRepository.save(MemberStockEntity.builder()
                        .memberStockNo(getMemberStockCode.getMemberStockNo())
                        .stockCode(getMemberStockCode.getStockCode())
                        .stockName(getMemberStockCode.getStockName())
                        .memberNo(getMemberStockCode.getMemberNo())
                        .memberStockAmount(sumAmount)
                        .memberStockAvgPrice(setAvgPriceDefault)
                        .memberStockAccPurchasePrice(getMemberStockCode.getMemberStockAccPurchasePrice())
                        .memberStockAccSellPrice(totalSellPrice)
                        .memberStockAccEarnRate(earnRate2)
                        .build()
                );

                // 추가하면서 임시리스트 목록 기반하여 매수 예정 내역 db에서 지운다.
                if(limitPriceOrderDto.getLimitPriceOrderNo() != null){
                    limitPriceOrderRepository.deleteAllByMemberNoAndLimitPriceOrderNo(limitPriceOrderDto.getMemberNo(), limitPriceOrderDto.getLimitPriceOrderNo());
                }

                log.info("매도 요청 완료");


            } else {  // 매도할 금액이 종가보다 크면 예약 목록에 등록합니다.
                //result = new LimitPriceOrderDto(limitPriceOrderRepository.saveAndFlush(limitPriceOrderDto.toEntity()));
                log.info("매도 예약 목록 등록");
                result = limitPriceOrderRepository.saveAndFlush(limitPriceOrderDto.toEntity());

                log.info("회원 날짜 변경 : {}", memberSimulationTime);
                MemberAssetEntity getMemberAsset = memberAssetRepository.findByMember_MemberNo(limitPriceOrderDto.getMemberNo());
                memberAssetRepository.save(MemberAssetEntity.builder()
                        .memberassetNo(getMemberAsset.getMemberassetNo())
                        .member(MemberEntity.builder().memberNo(limitPriceOrderDto.getMemberNo()).build())
                        .memberassetCurrentTime(memberSimulationTime)
                        .memberassetTotalAsset(getMemberAsset.getMemberassetTotalAsset())
                        .memberassetAvailableAsset(getMemberAsset.getMemberassetAvailableAsset())
                        .memberassetStockAsset(getMemberAsset.getMemberassetStockAsset())
                        .build());
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
