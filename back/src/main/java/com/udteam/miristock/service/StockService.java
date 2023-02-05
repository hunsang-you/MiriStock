package com.udteam.miristock.service;

import com.udteam.miristock.entity.StockEntity;
import com.udteam.miristock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public List<StockEntity> findByStockName(String keyword) {
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
