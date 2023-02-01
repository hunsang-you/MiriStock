package com.udteam.miristock.repository;

import com.udteam.miristock.entity.LimitPriceOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LimitPriceOrderRepository extends JpaRepository<LimitPriceOrderEntity, Integer> {

    List<LimitPriceOrderEntity> findAllByMemberNo (Integer memberNo);

}
