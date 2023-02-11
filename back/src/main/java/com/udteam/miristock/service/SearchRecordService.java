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
        List<SearchRecordEntity> articleEntityList = searchRecordRepository.findByMemberNoOrderBySearchNoDesc(memberNo);
        return articleEntityList;
    }

    @Transactional
    public Object save(SearchRecordEntity searchRecordEntity) {
        List<SearchRecordEntity> memberSearchResult = searchRecordRepository.findByMemberNoOrderBySearchNoDesc(searchRecordEntity.getMemberNo());
        String saveStockCode = searchRecordEntity.getStockCode();
        for (SearchRecordEntity recordEntity : memberSearchResult) {
            if (recordEntity.getStockCode().equals(saveStockCode)) {
                return "Duplicated StockCode : record save fail";
            }
        }
        if (memberSearchResult.size() >= 10){
            searchRecordRepository.deleteByMemberNoAndStockCode(searchRecordEntity.getMemberNo(), memberSearchResult.get(memberSearchResult.size()-1).getStockCode());
        }
        return searchRecordRepository.saveAndFlush(searchRecordEntity);
    }

    @Transactional
    public int delete(Integer memberNo, String stockCode) {
        return searchRecordRepository.deleteByMemberNoAndStockCode(memberNo, stockCode);
    }

}
