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

import java.sql.Array;
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
        log.info("type :{}",type);
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
        float stockDataFlucauationRateSum = 0.0f;
        Long stockDataPriceIncreasement = 0L;
        int size = stockDataMemberStockDtos.size();
        for (int i = 0; i < size; i++) {
            stockDataFlucauationRateSum += stockDataMemberStockDtos.get(i).getStockDataFlucauationRate();
            stockDataPriceIncreasement += stockDataMemberStockDtos.get(i).getStockDataPriceIncreasement() * stockDataMemberStockDtos.get(i).getMemberStockAmount();
        }
        return new StockRateAndPriceResponseDto(stockDataFlucauationRateSum / size, stockDataPriceIncreasement / size);
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
