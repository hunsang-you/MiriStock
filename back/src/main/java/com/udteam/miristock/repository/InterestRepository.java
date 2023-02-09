package com.udteam.miristock.repository;

import com.udteam.miristock.entity.InterestEntity;
import com.udteam.miristock.entity.StockDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InterestRepository extends JpaRepository<InterestEntity,Integer> {

    @Query(" SELECT s FROM StockDataEntity as s JOIN InterestEntity as i ON s.stockCode=i.stock.stockCode " +
            "where s.stockDataDate=:stockDataDate and i.member.memberNo=:memberNo ")
    List<StockDataEntity> selectInterestStock(@Param("stockDataDate") Integer stockDataDate, @Param("memberNo") Integer memberNo);

    @Transactional
    int deleteByMember_MemberNoAndStock_StockCode(Integer id, String stockCode);
}
