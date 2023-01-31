package com.udteam.miristock.repository;

import com.udteam.miristock.entity.InterestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<InterestEntity,Integer> {
    void deleteByMember_MemberNoAndStock_StockCode(int id,String stockCode);
}
