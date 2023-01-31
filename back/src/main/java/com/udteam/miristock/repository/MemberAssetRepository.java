package com.udteam.miristock.repository;


import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.entity.MemberAssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberAssetRepository extends JpaRepository<MemberAssetEntity,Integer> {
}
