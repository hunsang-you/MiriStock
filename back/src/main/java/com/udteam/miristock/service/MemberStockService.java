package com.udteam.miristock.service;

import com.udteam.miristock.dto.MemberStockDto;
import com.udteam.miristock.dto.StockDataMemberStockDto;
import com.udteam.miristock.dto.StockRateAndPriceResponseDto;
import com.udteam.miristock.entity.MemberStockEntity;
import com.udteam.miristock.entity.StockDataEntity;
import com.udteam.miristock.repository.MemberStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberStockService {

    private final MemberStockRepository memberStockRepository;

    // 회원 보유 주식 출력
    public List<?> findAll(Integer memberNo, Integer currentTime, String type) {
        List<Object[]> result = null;
        log.debug("type :{}",type);
        if (type.equals("price")) {
            result = memberStockRepository.findAllMemberStockListOrderByPrice(memberNo, currentTime);
        } else if (type.equals("rate")){
            result = memberStockRepository.findAllMemberStockListOrderByEarnRate(memberNo, currentTime);
        } else {
            result = memberStockRepository.findAllMemberStockListOrderByPrice(memberNo, currentTime);
        }
        return getObjects(result);
    }

    // 회원 보유 단건검색
    public List<?> findOne(Integer memberNo, Integer currentTime, String stockCode) {
        List<Object[]> result =  memberStockRepository.findOneMemberStock(memberNo, currentTime, stockCode);
        return getObjects(result);
    }

    // 전체 보유주식 등락률, 금액도 보여주기
    public StockRateAndPriceResponseDto getMemberStockRateAndPrice(Integer memberNo, Integer currentTime) {
        List<Object[]> result = memberStockRepository.findAllMemberStockList(memberNo, currentTime);
        List<StockDataMemberStockDto> stockDataMemberStockDtos = (List<StockDataMemberStockDto>) getObjects(result);

        // 이전금액 
        long stockDataAvgPriceSum = 0L;
        long stockDataClosingPriceSum = 0L;
        long stockdatapriceincreasement = 0L;
        long stockDataAmountSum = 0;
        float stockDataFlucauationRateSum = 0.0f;

        int size = stockDataMemberStockDtos.size();
        // 보유 주식이 없으므로 그냥 0으로 리턴한다.
        if (size == 0) {
            log.debug("main 보유주식 등락률, 등락금 보유주기에서 보유주식모록이 없음");
            return new StockRateAndPriceResponseDto(0.0f, 0L);
        }

        for (int i = 0; i < size; i++) {
            log.debug("stockDataMemberStockDtos : {} ", stockDataMemberStockDtos.get(i));
            // 이전금액 합계
            log.debug("stockDataMemberStockDtos.get(i).getMemberStockAvgPrice() : {}", stockDataMemberStockDtos.get(i).getMemberStockAvgPrice());
            log.debug("stockDataMemberStockDtos.get(i).getStockDataClosingPrice() : {} ", stockDataMemberStockDtos.get(i).getStockDataClosingPrice() );
            log.debug("stockDataMemberStockDtos.get(i).getMemberStockAmount() : {}", stockDataMemberStockDtos.get(i).getMemberStockAmount());
            stockDataAvgPriceSum += stockDataMemberStockDtos.get(i).getMemberStockAvgPrice() * stockDataMemberStockDtos.get(i).getMemberStockAmount();
            // 현재가 합계
            stockDataClosingPriceSum += stockDataMemberStockDtos.get(i).getStockDataClosingPrice() * stockDataMemberStockDtos.get(i).getMemberStockAmount();
            // 등락금액 더하기 (현재가 - 평매가) X 보유주식량 = 등락금액
            stockdatapriceincreasement += stockDataClosingPriceSum - stockDataAvgPriceSum;
            // 총 보유주식량 더하기
            stockDataAmountSum += stockDataMemberStockDtos.get(i).getMemberStockAmount();
        }
        // 전체 등락금액의 등락률 구하기
        log.debug("stockDataAvgPriceSum : {} ", stockDataAvgPriceSum);
        log.debug("stockDataClosingPriceSum : {}", stockDataClosingPriceSum);
        log.debug("stockdatapriceincreasement : {}", stockdatapriceincreasement);

        if( ((float)stockDataClosingPriceSum - (float)stockDataAvgPriceSum) == 0.0f){
            return new StockRateAndPriceResponseDto(0.0f, 0L);
        }
        stockDataFlucauationRateSum = (float)( ((float)stockDataClosingPriceSum - (float)stockDataAvgPriceSum) / (float)stockDataAvgPriceSum) * (float)100;

        log.debug("stockDataFlucauationRateSum : {} ", stockDataFlucauationRateSum);
        log.debug("stockdatapriceincreasement : {}", stockdatapriceincreasement);
        // error 분기
//        if (stockDataFlucauationRateSum == 0 & stockdatapriceincreasement != 0 ){
//            return new StockRateAndPriceResponseDto(0f, stockdatapriceincreasement);
//        } else if (stockDataFlucauationRateSum != 0f & stockdatapriceincreasement == 0 ){
//            return new StockRateAndPriceResponseDto(stockDataFlucauationRateSum, 0L);
//        }
        return new StockRateAndPriceResponseDto(stockDataFlucauationRateSum, stockdatapriceincreasement);
    }

    private List<?> getObjects(List<Object[]> result) {
        List<StockDataMemberStockDto> returnData = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            StockDataMemberStockDto data = new StockDataMemberStockDto(
                    (MemberStockEntity) Arrays.asList(result.get(i)).get(0), (StockDataEntity) Arrays.asList(result.get(i)).get(1));
            returnData.add(data);
        }
        return returnData;
    }

    // 회원 보유 주식 등록하기
    @Transactional
    public MemberStockDto save(MemberStockDto memberStockDto) {
        return new MemberStockDto(memberStockRepository.save(memberStockDto.toEntity()));
    }

    // 회원 보유 주식 삭제하기
    @Transactional
    public int delete(Integer memberNo, String stockCode) {
        return memberStockRepository.deleteByMemberNoAndStockCode(memberNo, stockCode);
    }

}
