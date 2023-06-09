package com.udteam.miristock.service;

import com.udteam.miristock.dto.MemberAssetDto;
import com.udteam.miristock.entity.MemberAssetEntity;
import com.udteam.miristock.entity.MemberEntity;
import com.udteam.miristock.entity.MemberStockEntity;
import com.udteam.miristock.entity.StockDataEntity;
import com.udteam.miristock.repository.MemberAssetRepository;
import com.udteam.miristock.repository.MemberStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberAssetService {
    private final MemberAssetRepository memberAssetRepository;
    private final MemberStockRepository memberStockRepository;

    public MemberAssetDto selectMemberAsset(Integer id) {
        return MemberAssetDto.of(memberAssetRepository.findById(id).get());
    }

    public MemberAssetDto updateMemberAsset(MemberAssetDto memberAssetDto) {

        MemberAssetEntity result = memberAssetRepository.save(MemberAssetEntity.builder()
                .memberassetNo(memberAssetDto.getMemberassetNo())
                .member(MemberEntity.builder().memberNo(memberAssetDto.getMemberNo()).build())
                .memberassetCurrentTime(memberAssetDto.getMemberassetCurrentTime())
                .memberassetTotalAsset(memberAssetDto.getMemberassetTotalAsset())
                .memberassetAvailableAsset(memberAssetDto.getMemberassetAvailableAsset())
                .memberassetStockAsset(memberAssetDto.getMemberassetStockAsset())
                .memberassetLastTotalAsset(memberAssetDto.getMemberassetLastTotalAsset())
                .build());
        return MemberAssetDto.of(result);
    }

//     회원 보유 주식 자산과 날짜 업데이트하기
    public MemberAssetDto updateMemberStockAsset(Integer memberNo, Integer currentTime, String updateType) {
        log.debug("시뮬레이션 날짜가 변경되면 보유 주식의 가격변동으로 회원 주식 자산도 변동하는 메서드 호출함.");
        // 회원의 보유 주식을 들고 오기
        MemberAssetEntity memberAssetEntity = memberAssetRepository.findByMember_MemberNo(memberNo);
        log.debug("memberAssetEntity : {}", memberAssetEntity);
        List<Object[]> memberStockList = memberStockRepository.findAllMemberStockListOrderByPrice(memberNo, currentTime);
//        List<StockDataMemberStockDto> stockDataMemberStockDtoList = new ArrayList<>();
//        Long sumDiff = 0L;
        Long stockAssetSum = 0L;
        for (Object[] objects : memberStockList){
            MemberStockEntity memberStockEntity = (MemberStockEntity) objects[0];
            StockDataEntity stockDataEntity = (StockDataEntity) objects[1];
//            stockDataMemberStockDtoList.add(new StockDataMemberStockDto(memberStockEntity, stockDataEntity));
            log.debug("memberStockEntity : {}", memberStockEntity);
            log.debug("stockDataEntity : {}", stockDataEntity);

//            // 회원의 한 종목 주식의 평균 매수가
//            Long memberStockAvgPrice = memberStockEntity.getMemberStockAvgPrice();
//            log.debug("한 종목 주식의 평균 매수가 memberStockAvgPrice :{} ", memberStockAvgPrice);
            // 해당 주식 날짜의 종가
            Long stockDataEntityStockDataClosingPrice = stockDataEntity.getStockDataClosingPrice();
            log.debug("한 종목 주식 해당날짜 종가 stockDataEntityStockDataClosingPrice : {}",stockDataEntityStockDataClosingPrice);
//            Long stockDataIncreasement = stockDataEntity.getStockDataPriceIncreasement();
//            log.debug("한 종목의 등락금액 : {}", stockDataIncreasement);
            Long memberStockAmount = memberStockEntity.getMemberStockAmount();
            log.debug("한 종목의 보유량 : {}", memberStockAmount);

            // 종가 - 평매가 (차익)
//            Long diffAvgPrice = (stockDataEntityStockDataClosingPrice - memberStockAvgPrice) * memberStockAmount;
//            log.debug("종가 - 평매가 차익 : {}",diffAvgPrice);
//            sumDiff += (stockDataIncreasement * memberStockAmount);
            stockAssetSum += (stockDataEntityStockDataClosingPrice * memberStockAmount);

        }
//        log.debug("sumDiff :  {}",sumDiff);
        log.debug("stockAssetSum : {}", stockAssetSum);
        // 멤버 전날 총 자산에 오늘 자산 데이터 업데이트하기 (전날보다 얼마나 자산이 증가했는지 계산하기 위한 데이터)
        // 당일건 거래는 이전 날짜 총 자산을 업데이트 할 필요없음
        if (updateType.equals("todayLimitPriceOrder")){
            memberAssetEntity.setMemberassetLastTotalAsset(memberAssetEntity.getMemberassetLastTotalAsset());
        }
//        // 시뮬레이션 날짜 업데이트임..
        else {
            log.debug("모든 보유 주식 자산 반영후 시뮬레이션 날짜 업데이트시 어제 날짜 반영함. memberAssetEntity.getMemberassetTotalAsset() : {}", memberAssetEntity.getMemberassetTotalAsset());
            memberAssetEntity.setMemberassetLastTotalAsset(memberAssetEntity.getMemberassetTotalAsset());
        }
        // 들고온 보유 주식과 데이터 기반으로 회원의 보유 주식 자산을 업데이트 하기
//        memberAssetEntity.setMemberassetStockAsset(memberAssetEntity.getMemberassetStockAsset() + sumDiff);
//        log.debug("memberAssetEntity.getMemberassetStockAsset() + sumDiff 시뮬레이션 날짜 변경후 주식 자산 : {}" ,memberAssetEntity.getMemberassetStockAsset() + sumDiff);
        if(stockAssetSum < 0) {
            log.warn("MemberAssetService Class > updateMemberStockAsset : stockAssetSum(변경될 회원자산 주식자산) 이 마이너스 감지");
            stockAssetSum = 0L;
        }

        memberAssetEntity.setMemberassetStockAsset(stockAssetSum);
        log.debug("stockAssetSum += stockDataEntityStockDataClosingPrice * memberStockAmount" ,stockAssetSum);

        memberAssetEntity.setMemberassetAvailableAsset(memberAssetEntity.getMemberassetAvailableAsset());

//        memberAssetEntity.setMemberassetTotalAsset(memberAssetEntity.getMemberassetTotalAsset() + sumDiff);
//        log.debug("memberAssetEntity.getMemberassetTotalAsset() + sumDiff 시뮬레이션 날짜 변경후 총 자산 : {}", memberAssetEntity.getMemberassetTotalAsset() + sumDiff);
        memberAssetEntity.setMemberassetTotalAsset(memberAssetEntity.getMemberassetAvailableAsset() + stockAssetSum);
        log.debug("memberAssetEntity.getMemberassetAvailableAsset() + stockAssetSum 시뮬레이션 날짜 변경후 총 자산 : {}",memberAssetEntity.getMemberassetAvailableAsset() + stockAssetSum);

        memberAssetEntity.setMemberassetCurrentTime(currentTime);
        log.debug("시뮬레이션 날짜변경하면서 보유 주식 금액 반영함 : {}",currentTime);
        MemberAssetEntity result = memberAssetRepository.save(memberAssetEntity);
        log.debug("회원 보유 주식 자산 업데이트됨 : {}", result);
        return MemberAssetDto.of(result);
    }

//    public RequestSimulationDto updateMemberAssetTime(RequestSimulationDto requestSimulationDto) {
//        return RequestSimulationDto.memberEntityToSimulationResponseDto(
//                memberAssetRepository.save(
//                        MemberAssetEntity.builder()
//                                .member(MemberEntity.builder().memberNo(requestSimulationDto.getMemberNo()).build())
//                                .memberassetCurrentTime(requestSimulationDto.getMemberassetCurrentTime())
//                                .build()
//                ));
//    }

//        public MemberAssetDto updateMemberAssetTime(MemberAssetDto memberAssetDto) {
//        return MemberAssetDto.memberEntityToSimulationResponseDto(
//                memberAssetRepository.save(
//                        MemberAssetEntity.builder()
//                                .member(MemberEntity.builder().memberNo(memberAssetDto.getMemberNo()).build())
//                                .memberassetCurrentTime(memberAssetDto.getMemberassetCurrentTime())
//                                .build()
//                ));
//    }

}
