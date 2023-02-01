package com.udteam.miristock.repository;

import com.udteam.miristock.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
public interface MemberRepository extends JpaRepository<MemberEntity,Long>{
    MemberEntity findByMemberEmail(String email);

    @Transactional
    int deleteByMemberEmail(String email);
    String findRefreshtokenByMemberEmail(String email);

}
