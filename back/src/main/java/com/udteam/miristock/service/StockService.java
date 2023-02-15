package com.udteam.miristock.service;

import com.udteam.miristock.repository.StockRepository;
import com.udteam.miristock.util.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public Object findByStockName(String keyword) {
        if(keyword == null || keyword.equals("")) return ErrorMessage.PARAMETER_NULL;
        try {
            Double.parseDouble(keyword);
            return stockRepository.findByStockCodeStartingWithOrderByStockCodeAsc(keyword);
        } catch (NumberFormatException e){
            return stockRepository.findByStockNameStartingWithOrderByStockCodeAsc(keyword);
        } catch (Exception e) {
            return null;
        }
    }
}
