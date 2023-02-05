package com.udteam.miristock.repository;

import com.udteam.miristock.entity.MemberStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberStockRepository extends JpaRepository<MemberStockEntity, Integer> {

    List<MemberStockEntity> findAllByMemberNo(Integer memberNo);

    void deleteByMemberNoAndStockCode(Integer memberNo, String stockCode);

}
