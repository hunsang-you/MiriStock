package com.udteam.miristock.controller;

import com.udteam.miristock.config.ErrorMessage;
import com.udteam.miristock.dto.CommentRequestDto;
import com.udteam.miristock.dto.LimitPriceOrderDto;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.service.CommentService;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Integer> save(@RequestHeader String Authorization, @RequestBody CommentRequestDto commentRequestDto) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            commentRequestDto.setMemberNo(m.getMemberNo());
            return ResponseEntity.ok().body(commentService.save(commentRequestDto));
        }
    }

    @PutMapping
    public ResponseEntity<Integer> update(@RequestHeader String Authorization, @RequestBody CommentRequestDto commentRequestDto) {
        log.info("CommentRequestDto : {}", commentRequestDto);
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            commentRequestDto.setMemberNo(m.getMemberNo());
            return ResponseEntity.ok().body(commentService.save(commentRequestDto));
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestHeader String Authorization, @RequestParam Integer commentNo) {
        log.info("CommentRequestDto : {}", commentNo);
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            commentService.delete(commentNo);
            return ResponseEntity.ok().body(null);
        }
    }
}

