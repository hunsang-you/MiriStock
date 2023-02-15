package com.udteam.miristock.service;

import com.udteam.miristock.dto.StockDataInfoMapping;
import com.udteam.miristock.dto.StockDataResponseDto;
import com.udteam.miristock.repository.StockDataCustomRepository;
import com.udteam.miristock.repository.StockDataRepository;
import com.udteam.miristock.util.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockDataService {

    private final StockDataRepository stockDataRepository;
    private final StockDataCustomRepository stockDataCustomRepository;

    // 거래량 가장 많은 5개 출력
    public List<StockDataResponseDto> findTop5AmountDesc(Integer stockDataDate) {
        return stockDataRepository.findTop5ByStockDataDateAndStockDataAmountNotOrderByStockDataAmountDesc(stockDataDate, 0L)
        .stream().map(StockDataResponseDto::new)
        .collect(Collectors.toList());
    }

    // 등락률 가장 낮은 5개 순서대로 가져오기
    public List<StockDataResponseDto> findTop5BySFlucauationRateAsc(Integer stockDataDate){
        return stockDataRepository.findTop5ByStockDataDateAndStockDataAmountNotOrderByStockDataFlucauationRateAsc(stockDataDate, 0L)
        .stream().map(StockDataResponseDto::new)
        .collect(Collectors.toList());
    }

    // 등락률 가장 높은 순서대로 5개 출력
    public List<StockDataResponseDto> findTop5BySFlucauationRateDesc(Integer stockDataDate){
        return stockDataRepository.findTop5ByStockDataDateAndStockDataAmountNotOrderByStockDataFlucauationRateDesc(stockDataDate, 0L)
        .stream().map(StockDataResponseDto::new)
        .collect(Collectors.toList());
    }

    public List<StockDataResponseDto> findStockData(Integer searchStartDate, Integer searchEndDate, String searchStockCode){
        return stockDataCustomRepository.findStockData(searchStartDate, searchEndDate, searchStockCode)
                .stream().map(StockDataResponseDto::new)
                .collect(Collectors.toList());
    }

    // 날짜 기반 주식 종목 코드, 주식 종목명 검색 서비스
    public Object findByStock(String keyword, Integer stockDataDate) {
        if(keyword == null || keyword.equals("")) return ErrorMessage.PARAMETER_NULL;
        try {
            Double.parseDouble(keyword);
            return stockDataRepository.findAllByStockCodeStartingWithAndStockDataDateOrderByStockCodeAsc(keyword, stockDataDate);
        } catch (NumberFormatException e){
            return stockDataRepository.findAllByStockNameStartingWithAndStockDataDateOrderByStockCodeAsc(keyword, stockDataDate);
        } catch (Exception e) {
            return null;
        }
    }

    public StockDataInfoMapping findTop1ByStockDataDate(Integer stockDataDate){
        return stockDataRepository.findTop1ByStockDataDate(stockDataDate);
    }

}
