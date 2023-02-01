package com.udteam.miristock.repository;

import com.udteam.miristock.entity.Deal;
import com.udteam.miristock.entity.StockDealEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockDealCustomRepository {

    List<StockDealEntity> findAllByMemberNoAndStockDealType(Integer memberNo, Deal stockDealType);

}
