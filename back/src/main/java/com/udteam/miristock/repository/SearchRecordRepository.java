package com.udteam.miristock.repository;

import com.udteam.miristock.entity.SearchRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRecordRepository extends JpaRepository<SearchRecordEntity, Integer> {

    List<SearchRecordEntity> findByMemberNo (Integer memberNo);

    int deleteByMemberNoAndStockCode(Integer memberNo, String stockCode);
}
