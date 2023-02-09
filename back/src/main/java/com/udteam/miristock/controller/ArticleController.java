package com.udteam.miristock.controller;

import com.udteam.miristock.util.ErrorMessage;
import com.udteam.miristock.dto.ArticleResponseDto;
import com.udteam.miristock.dto.ArticleRequestDto;
import com.udteam.miristock.dto.ArticleCUDResponseDto;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.service.ArticleService;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.util.HeaderUtil;
import com.udteam.miristock.util.ReturnMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/qna")
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

    @GetMapping("/{articleno}")
    public ResponseEntity<ArticleResponseDto> findOne(@RequestHeader String Authorization, @PathVariable Integer articleno){
        log.info("articel 목록");
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.ok().body(articleService.findOne(articleno));
        }
    }

    @PostMapping
    public ResponseEntity<ArticleCUDResponseDto> save(@RequestHeader String Authorization, @RequestBody ArticleRequestDto articleRequestDto) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        log.info("articleRequestDto : {}", articleRequestDto);
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            articleRequestDto.setMemberNo(m.getMemberNo());
            articleRequestDto.setMemberNickname(m.getMemberNickname());
            return ResponseEntity.ok().body(articleService.save(articleRequestDto));
        }
    }

    @PutMapping
    public ResponseEntity<ArticleCUDResponseDto> update(@RequestHeader String Authorization, @RequestBody ArticleRequestDto articleRequestDto) {
        log.info("articleRequestDto : {}", articleRequestDto);
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            articleRequestDto.setMemberNo(m.getMemberNo());
            articleRequestDto.setMemberNickname(m.getMemberNickname());
            return ResponseEntity.ok().body(articleService.save(articleRequestDto));
        }
    }

    @DeleteMapping("/{articleno}")
    public ResponseEntity<String> delete(@RequestHeader String Authorization, @PathVariable Integer articleno) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if(articleService.delete(m.getMemberNo(), articleno) == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ReturnMessage.DELETE_FAIL);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ReturnMessage.DELETE_SUCCESS);

    }

}
