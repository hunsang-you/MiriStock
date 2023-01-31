package com.udteam.miristock.service;

import com.udteam.miristock.entity.Stock;
import com.udteam.miristock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StockService {

    private final StockRepository stockRepository;

    public List<Stock> findByStockName(String stockName) {
        return stockRepository.findByStockNameStartingWith(stockName);
    }
}
