package com.udteam.miristock.repository;

import com.udteam.miristock.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockEntity,String> {

}
