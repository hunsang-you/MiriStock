package com.udteam.miristock.repository;

import com.udteam.miristock.entity.Deal;
import com.udteam.miristock.entity.LimitPriceOrderEntity;

import java.util.List;

public interface LimitPriceOrderCustomRepository {

    List<LimitPriceOrderEntity> findAllByMemberNoAndLimitPriceOrderType (Integer memberNo, Deal limitPriceOrderType);


}
