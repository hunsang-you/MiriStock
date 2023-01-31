package com.udteam.miristock.service;

import com.udteam.miristock.entity.SearchRecord;
import com.udteam.miristock.repository.SearchRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchRecordService {

    private final SearchRecordRepository searchRecordRepository;

    public List<SearchRecord> findByMemberNo(Long memberNo) {
        List<SearchRecord> articleEntityList = searchRecordRepository.findByMemberNo(memberNo);
        return articleEntityList;
    }

    @Transactional
    public Long save(SearchRecord searchRecord) {
        return searchRecordRepository.save(searchRecord).getMemberNo();
    }

    @Transactional
    public void delete(Long searchNo) {
        searchRecordRepository.delete(SearchRecord.builder().searchNo(searchNo).build());
    }


}
