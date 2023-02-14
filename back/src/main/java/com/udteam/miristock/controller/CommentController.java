package com.udteam.miristock.controller;

import com.udteam.miristock.dto.*;
import com.udteam.miristock.entity.Role;
import com.udteam.miristock.util.ErrorMessage;
import com.udteam.miristock.service.CommentService;
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

@Slf4j
@RestController
@RequestMapping("/qna/comment")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "QnA 댓글")
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "댓글 등록", description = "QnA 게시글의 댓글을 등록합니다.", tags = { "Comment" })
    public ResponseEntity<CommentResponseDto> save(@RequestHeader String Authorization, @RequestBody CommentRequestDto commentRequestDto) {
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            commentRequestDto.setMemberNo(m.getMemberNo());
            commentRequestDto.setMemberNickname(m.getMemberNickname());
            return ResponseEntity.ok().body(commentService.save(commentRequestDto));
        }
    }

    @PutMapping
    @Operation(summary = "댓글 수정", description = "QnA 게시글의 댓글을 수정합니다.", tags = { "Comment" })
    public ResponseEntity<CommentResponseDto> update(@RequestHeader String Authorization, @RequestBody CommentRequestDto commentRequestDto) {
        log.info("CommentRequestDto : {}", commentRequestDto);
        MemberAdminDto m = memberService.selectOneMemberAllInfo(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else if (m.getROLE().equals(Role.ADMIN)) {
            CommentResponseDto getCommentResponseDto = commentService.findOne(commentRequestDto.getCommentNo());
            commentRequestDto.setMemberNo(getCommentResponseDto.getMemberNo());
            commentRequestDto.setMemberNickname(getCommentResponseDto.getMemberNickname());
            return ResponseEntity.ok().body(commentService.save(commentRequestDto));
        } else {
            commentRequestDto.setMemberNo(m.getMemberNo());
            commentRequestDto.setMemberNickname(m.getMemberNickname());
            return ResponseEntity.ok().body(commentService.save(commentRequestDto));
        }
    }

    @DeleteMapping("/{commentno}")
    @Operation(summary = "댓글 삭제", description = "QnA 게시글의 댓글을 삭제합니다.", tags = { "Comment" })
    public ResponseEntity<String> delete(@RequestHeader String Authorization, @PathVariable Integer commentno) {
        log.info("CommentRequestDto : {}", commentno);
        MemberAdminDto m = memberService.selectOneMemberAllInfo(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.TOKEN_EXPIRE);
        } else if (m.getROLE().equals(Role.ADMIN)){
            if(commentService.deleteAdminMode(commentno) == 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ReturnMessage.DELETE_FAIL);
            return ResponseEntity.status(HttpStatus.OK).body(ReturnMessage.DELETE_SUCCESS);
        } else {
            if (commentService.delete(m.getMemberNo(), commentno) == 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ReturnMessage.DELETE_FAIL);
            return ResponseEntity.status(HttpStatus.OK).body(ReturnMessage.DELETE_SUCCESS);
        }
    }
}

