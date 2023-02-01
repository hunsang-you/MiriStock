package com.udteam.miristock.controller;

import com.udteam.miristock.entity.SearchRecordEntity;
import com.udteam.miristock.service.SearchRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/searchrecord")
@RequiredArgsConstructor
public class SearchRecordController {

    private final SearchRecordService searchRecordService;

    // 검색기록 출력
    @GetMapping("/list")
    public ResponseEntity<List<SearchRecordEntity>> save(@RequestHeader(value = "memberNo" ) Long memberNo) throws Exception{
        log.info("주식 종목 검색 기록 출력");
        log.info("memberNo : {} ", memberNo);
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
    public Long save(@RequestBody SearchRecordEntity searchRecordEntity){
        log.info("searchRecord : {}", searchRecordEntity);
        return searchRecordService.save(searchRecordEntity);
    }

    //검색기록 제거
    @DeleteMapping
    public void delete(@RequestHeader(value = "searchNo" ) Long searchNo){
        log.info("searchNo : {}", searchNo);
        searchRecordService.delete(searchNo);
    }

}
