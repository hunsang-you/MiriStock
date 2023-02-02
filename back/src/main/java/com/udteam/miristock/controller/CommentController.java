package com.udteam.miristock.controller;

import com.udteam.miristock.dto.CommentRequestDto;
import com.udteam.miristock.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public Integer save(@RequestBody CommentRequestDto commentRequestDto){
        log.info("CommentRequestDto : {}", commentRequestDto);
        return commentService.save(commentRequestDto);
    }

    @PutMapping
    public Integer update(@RequestBody CommentRequestDto commentRequestDto){
        log.info("CommentRequestDto : {}", commentRequestDto);
        return commentService.save(commentRequestDto);
    }

    @DeleteMapping
    public void delete(@RequestParam Integer commentNo){
        log.info("CommentRequestDto : {}", commentNo);
        commentService.delete(commentNo);
    }
}

