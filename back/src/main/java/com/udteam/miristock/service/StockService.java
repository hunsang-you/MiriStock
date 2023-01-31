package com.udteam.miristock.service;

import com.udteam.miristock.entity.StockEntity;
import com.udteam.miristock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StockService {

    private final StockRepository stockRepository;

    public List<StockEntity> findByStockName(String stockName) {
        return stockRepository.findByStockNameStartingWith(stockName);
    }
}
