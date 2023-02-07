package com.udteam.miristock.repository;

import com.udteam.miristock.entity.MemberStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberStockRepository extends JpaRepository<MemberStockEntity, Integer> {

    // 회원 보유 주식 전부 출력하기
    List<MemberStockEntity> findAllByMemberNo(Integer memberNo);
    
    // 회원 보유 주식 삭제하기
    void deleteByMemberNoAndStockCode(Integer memberNo, String stockCode);

    // 시뮬레이션 종료시 가장 많은 수익금과 손실금을 출력하는 쿼리문 =======
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

    // 회원의 보유 주식 가져오기
    MemberStockEntity findByMemberNoAndStockCode(Integer memberNo, String stockCode);
}
