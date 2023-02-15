package com.udteam.miristock.controller;

import com.udteam.miristock.dto.*;
import com.udteam.miristock.entity.Role;
import com.udteam.miristock.util.ErrorMessage;
import com.udteam.miristock.service.ArticleService;
import com.udteam.miristock.service.MemberService;
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
@RequestMapping("/qna")
@RequiredArgsConstructor
@Tag(name = "QnA", description = "QnA 게시판")
public class ArticleController {

    private final ArticleService articleService;
    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "QnA 게시글 목록", description = "QnA 게시글 목록과 댓글을 전체 출력합니다.", tags = { "QnA" })
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

    @GetMapping("/list")
    @Operation(summary = "QnA 게시글 목록", description = "QnA 게시글 목록과 댓글을 전체 출력합니다.", tags = { "QnA" })
    public ResponseEntity<List<ArticleResponseDto>> findArticleList(@RequestHeader String Authorization, @RequestParam(value = "index", required = false) Integer index){
        if(index == null) {index = 0;}
        log.info("articel 목록");
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.ok().body(articleService.findArticleList(index));
        }
    }

    @GetMapping("/{articleno}")
    @Operation(summary = "QnA 게시글 상세조회", description = "QnA 게시글 번호를 단건 출력합니다.", tags = { "QnA" })
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

    @GetMapping("/search")
    @Operation(summary = "QnA 게시글 검색", description = "QnA 게시글 제목과 내용이 일치한 게시물을 출력합니다.", tags = { "QnA" })
    public ResponseEntity<List<ArticleResponseDto>> findSearchArticle(@RequestHeader String Authorization, @RequestParam(name = "keyword", required = false) String keyword){
        log.info("articel 검색 키워드 : {}", keyword);
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            if (keyword == null){
                return ResponseEntity.ok().body(articleService.findAll());
            }
            return ResponseEntity.ok().body(articleService.findSearchArticle(keyword));
        }
    }

    @PostMapping
    @Operation(summary = "QnA 게시글 등록", description = "QnA 게시글을 등록합니다.", tags = { "QnA" })
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
    @Operation(summary = "QnA 게시글 수정", description = "QnA 게시글을 수정합니다.", tags = { "QnA" })
    public ResponseEntity<ArticleCUDResponseDto> update(@RequestHeader String Authorization, @RequestBody ArticleRequestDto articleRequestDto) {
        log.info("articleRequestDto : {}", articleRequestDto);
        // 요청자
        MemberAdminDto m = memberService.selectOneMemberAllInfo(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else if (m.getROLE().equals(Role.ADMIN)){
            ArticleResponseDto getArticleResponseDto = articleService.findOne(articleRequestDto.getArticleNo());
            articleRequestDto.setMemberNo(getArticleResponseDto.getMemberNo());
            articleRequestDto.setMemberNickname(getArticleResponseDto.getMemberNickname());
            return ResponseEntity.ok().body(articleService.save(articleRequestDto));
        } else {
            articleRequestDto.setMemberNo(m.getMemberNo());
            articleRequestDto.setMemberNickname(m.getMemberNickname());
            return ResponseEntity.ok().body(articleService.save(articleRequestDto));
        }
    }

    @DeleteMapping("/{articleno}")
    @Operation(summary = "QnA 게시글 삭제", description = "QnA 게시글을 삭제합니다.", tags = { "QnA" })
    public ResponseEntity<String> delete(@RequestHeader String Authorization, @PathVariable Integer articleno) {
        MemberAdminDto m = memberService.selectOneMemberAllInfo(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.TOKEN_EXPIRE);
        } else if (m.getROLE().equals(Role.ADMIN)){
            if(articleService.deleteAdminMode(articleno) == 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ReturnMessage.DELETE_FAIL);
            return ResponseEntity.status(HttpStatus.OK).body(ReturnMessage.DELETE_SUCCESS);
        } else{
            if(articleService.delete(m.getMemberNo(), articleno) == 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ReturnMessage.DELETE_FAIL);
            return ResponseEntity.status(HttpStatus.OK).body(ReturnMessage.DELETE_SUCCESS);
        }


    }

}
