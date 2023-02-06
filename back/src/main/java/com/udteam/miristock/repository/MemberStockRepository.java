package com.udteam.miristock.repository;

import com.udteam.miristock.entity.MemberStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberStockRepository extends JpaRepository<MemberStockEntity, Integer> {

    List<MemberStockEntity> findAllByMemberNo(Integer memberNo);
    void deleteByMemberNoAndStockCode(Integer memberNo, String stockCode);

    // 위 아래 실행속도가 같음...
    @Query(" SELECT m From MemberStockEntity as m where m.memberStockAccEarnPrice = " +
            " (select min(m.memberStockAccEarnPrice) from m)  " +
            "  AND m.memberStockAmount = 0")
    MemberStockEntity findTop1ByMemberNoAndMemberStockAmountOrderByMemberStockAccEarnPriceAsc(Integer memberNo, Long stockAmount);
//    MemberStockEntity findTop1ByMemberNoAndMemberStockAmountOrderByMemberStockAccEarnPriceAsc(Integer memberNo, Long stockAmount);
    @Query(" SELECT m From MemberStockEntity as m where m.memberStockAccEarnPrice = " +
            " (select max(m.memberStockAccEarnPrice) from  m)  " +
            "  AND m.memberStockAmount = 0")
    MemberStockEntity findTop1ByMemberNoAndMemberStockAmountOrderByMemberStockAccEarnPriceDesc(Integer memberNo, Long stockAmount);

}
