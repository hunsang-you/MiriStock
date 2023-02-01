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

    public List<SearchRecordEntity> findByMemberNo(Long memberNo) {
        List<SearchRecordEntity> articleEntityList = searchRecordRepository.findByMemberNo(memberNo);
        return articleEntityList;
    }

    @Transactional
    public Long save(SearchRecordEntity searchRecordEntity) {
        return searchRecordRepository.save(searchRecordEntity).getMemberNo();
    }

    @Transactional
    public void delete(Long searchNo) {
        searchRecordRepository.delete(SearchRecordEntity.builder().searchNo(searchNo).build());
    }

}
