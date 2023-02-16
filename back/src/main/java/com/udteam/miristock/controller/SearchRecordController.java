package com.udteam.miristock.controller;

import com.udteam.miristock.util.ErrorMessage;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.entity.SearchRecordEntity;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.service.SearchRecordService;
import com.udteam.miristock.util.HeaderUtil;
import com.udteam.miristock.util.ReturnMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/searchrecord")
@RequiredArgsConstructor
@Tag(name = "SearchRecord", description = "회원 주식 검색 기록")
public class SearchRecordController {

    private final SearchRecordService searchRecordService;
    private final MemberService memberService;

    // 검색기록 출력
    @GetMapping("/list")
    @Operation(summary = "주식 검색 기록 목록 출력", description = "회원의 주식 검색 기록 목록을 출력한다.", tags = { "SearchRecord" })
    public ResponseEntity<List<SearchRecordEntity>> findAllList(@RequestHeader String Authorization) throws Exception{
        log.debug("주식 종목 검색 기록 출력 호출됨");
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        log.info("회원 : {} | /searchrecord/list GET API호출 : 회원 검색기록 목록 출력", m);
        if (m == null){
            log.debug(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            log.debug("memberNo : {} ", m.getMemberNo());
            return ResponseEntity.ok().body(searchRecordService.findByMemberNo(m.getMemberNo()));
        }
    }

    // 검색기록 등록
    @PostMapping
    @Operation(summary = "주식 검색 기록 등록", description = "회원의 주식 검색 기록을 등록한다.", tags = { "SearchRecord" })
    public ResponseEntity<?> save(@RequestHeader String Authorization, @RequestBody SearchRecordEntity searchRecordEntity){
        log.debug("주식 종목 검색 기록 등록 호출됨 searchRecordEntity : {} ", searchRecordEntity);
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        log.info("회원 : {} | /searchrecord/list POST API호출 : 회원 검색기록 등록", m);
        if (m == null){
            log.debug(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            searchRecordEntity.setMemberNo(m.getMemberNo());
            log.debug("memberNo : {} ", m.getMemberNo());
            log.debug("searchRecord : {}", searchRecordEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(searchRecordService.save(searchRecordEntity));
        }
    }

    //검색기록 제거
    @DeleteMapping("/{stockCode}")
    @Operation(summary = "주식 검색 기록 삭제", description = "회원의 주식 검색 기록을 삭제한다.", tags = { "SearchRecord" })
    public ResponseEntity<String> delete(@RequestHeader String Authorization, @PathVariable String stockCode){
//        log.debug("검색기록 제거 호출됨 stockCode : {}", stockCode);
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        log.info("회원 : {} | /searchrecord/{stockCode} DELETE API호출 : 회원 검색기록 삭제", m);
        if (m == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.TOKEN_EXPIRE);
        }

        if (searchRecordService.delete(m.getMemberNo(), stockCode) == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ReturnMessage.DELETE_FAIL);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ReturnMessage.DELETE_SUCCESS);
    }

}
