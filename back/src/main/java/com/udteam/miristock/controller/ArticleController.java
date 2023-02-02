package com.udteam.miristock.controller;

import com.udteam.miristock.config.ErrorMessage;
import com.udteam.miristock.dto.ArticleResponseDto;
import com.udteam.miristock.dto.ArticleRequestDto;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.service.ArticleService;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<ArticleResponseDto>> findAll(@RequestHeader String Authorization){
        log.info("articel 목록");
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.ok().body(articleService.findAll());
        }
    }

    @GetMapping
    public ResponseEntity<ArticleResponseDto> findOne(@RequestHeader String Authorization, @PathVariable Integer articleNo){
        log.info("articel 목록");
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.ok().body(articleService.findOne(articleNo));
        }
    }

    public ResponseEntity<Integer> save(@RequestHeader String Authorization, @RequestBody ArticleRequestDto articleRequestDto) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        log.info("articleRequestDto : {}", articleRequestDto);
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.ok().body(articleService.save(articleRequestDto));
        }
    }

    public ResponseEntity<Integer> update(@RequestHeader String Authorization, @RequestBody ArticleRequestDto articleRequestDto) {
        log.info("articleRequestDto : {}", articleRequestDto);
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            articleRequestDto.setMemberNo(m.getMemberNo());
            return ResponseEntity.ok().body(articleService.save(articleRequestDto));
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestHeader String Authorization, @RequestHeader Integer articleNo) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            articleService.delete(articleNo);
            return ResponseEntity.ok().body(null);
        }
    }

}
