package com.udteam.miristock.controller;

import com.udteam.miristock.dto.CommentRequestDto;
import com.udteam.miristock.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {
    private Logger logger = LoggerFactory.getLogger(ArticleController.class);

    private final CommentService commentService;

    @PostMapping
    public Long save(@RequestBody CommentRequestDto commentRequestDto){
        logger.info("CommentRequestDto : {}", commentRequestDto);
        return commentService.save(commentRequestDto);
    }

    @PutMapping
    public Long update(@RequestBody CommentRequestDto commentRequestDto){
        logger.info("CommentRequestDto : {}", commentRequestDto);
        return commentService.save(commentRequestDto);
    }

    @DeleteMapping
    public void delete(@RequestParam Long commentNo){
        logger.info("CommentRequestDto : {}", commentNo);
        commentService.delete(commentNo);
    }
}

