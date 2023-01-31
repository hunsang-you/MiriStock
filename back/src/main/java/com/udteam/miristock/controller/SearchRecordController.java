package com.udteam.miristock.controller;

import com.udteam.miristock.entity.SearchRecord;
import com.udteam.miristock.service.SearchRecordService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/searchrecord")
public class SearchRecordController {

    private final SearchRecordService searchRecordService;

    private final Logger logger = LoggerFactory.getLogger(SearchRecordController.class);

    // 검색기록 출력
    @GetMapping("/list")
    public ResponseEntity<List<SearchRecord>> save(@RequestHeader(value = "memberNo" ) Long memberNo) throws Exception{
        logger.info("주식 종목 검색 기록 출력");
        logger.info("memberNo : {} ", memberNo);
        return ResponseEntity.ok().body(searchRecordService.findByMemberNo(memberNo));
    }

//    @GetMapping("/list/{memberNo}")
//    public ResponseEntity<List<SearchRecord>> save(@PathVariable Long memberNo) throws Exception{
//        logger.info("주식 종목 검색 기록 출력");
//        logger.info("memberNo : {} ", memberNo);
//        return ResponseEntity.ok().body(searchRecordService.findByMemberNo(memberNo));
//    }

    // 검색기록 등록
    @PostMapping
    public Long save(@RequestBody SearchRecord searchRecord){
        logger.info("searchRecord : {}", searchRecord);
        return searchRecordService.save(searchRecord);
    }

    //검색기록 제거
    @DeleteMapping
    public void delete(@RequestHeader(value = "searchNo" ) Long searchNo){
        logger.info("searchNo : {}", searchNo);
        searchRecordService.delete(searchNo);
    }

}
