package com.udteam.miristock.repository;

import com.udteam.miristock.entity.FinancialstatementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialstatementRepository extends JpaRepository<FinancialstatementEntity, Integer> {

    List<FinancialstatementEntity> findByStockCode(String stockCode);

}
