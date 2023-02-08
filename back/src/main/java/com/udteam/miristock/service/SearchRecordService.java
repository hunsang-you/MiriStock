package com.udteam.miristock.service;

import com.udteam.miristock.entity.SearchRecordEntity;
import com.udteam.miristock.repository.SearchRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchRecordService {

    private final SearchRecordRepository searchRecordRepository;

    public List<SearchRecordEntity> findByMemberNo(Integer memberNo) {
        List<SearchRecordEntity> articleEntityList = searchRecordRepository.findByMemberNo(memberNo);
        return articleEntityList;
    }

    @Transactional
    public SearchRecordEntity save(SearchRecordEntity searchRecordEntity) {
        return searchRecordRepository.saveAndFlush(searchRecordEntity);
    }

    @Transactional
    public void delete(Integer memberNo, String stockCode) {
        searchRecordRepository.deleteByMemberNoAndStockCode(memberNo, stockCode);
    }

}
