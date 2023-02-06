package com.udteam.miristock.service;

import com.udteam.miristock.dto.SimulEndDto;
import com.udteam.miristock.dto.StockDataInfoMapping;
import com.udteam.miristock.repository.MemberAssetRepository;
import com.udteam.miristock.repository.MemberStockRepository;
import com.udteam.miristock.repository.StockDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.udteam.miristock.service.InformationService.AddDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulationService {

    private final MemberAssetRepository memberAssetRepository;
    private final MemberStockRepository memberStockRepository;
    private final StockDataRepository stockDataRepository;

    @Transactional(readOnly = true)
    public SimulEndDto resultSimulation(Integer memberNo){
        return new SimulEndDto(
                memberAssetRepository.findById(memberNo).get(),
                memberStockRepository.findTop1ByMemberNoAndMemberStockAmountOrderByMemberStockAccEarnPriceAsc(memberNo, 0L),
                memberStockRepository.findTop1ByMemberNoAndMemberStockAmountOrderByMemberStockAccEarnPriceDesc(memberNo, 0L)
                );
    }

    @Transactional
    public void updateSimulationDate(Integer memberNo, Integer simulDate){

        Integer memberDate = memberAssetRepository.findById(memberNo).get().getMemberassetCurrentTime();
        log.info("회원의 시뮬레이션 시간 : {}", memberDate);
        String memberDateStr = null;

//        while(true){
            // 날짜 먼저 더하기
            memberDateStr = AddDate(String.valueOf(memberDate), 0,0, simulDate);

            // 해당 날짜 데이터가 있는지 확인하기
            Integer memberDateInt = Integer.parseInt(memberDateStr);
            StockDataInfoMapping result = stockDataRepository.findTop1ByStockDataDate(memberDateInt);
            if(result.getStockDataDate() == null) {
                log.info("뽑아낸 결과가 null 입니다.");
            } else{
            log.info("뽑아낸 결과 : {}", result.getStockDataDate());
            }

    }


}
