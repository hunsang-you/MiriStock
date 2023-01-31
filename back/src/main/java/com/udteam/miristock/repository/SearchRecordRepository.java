package com.udteam.miristock.repository;

import com.udteam.miristock.entity.SearchRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRecordRepository extends JpaRepository<SearchRecord, Long> {

    List<SearchRecord> findByMemberNo (Long memberNo);
}
