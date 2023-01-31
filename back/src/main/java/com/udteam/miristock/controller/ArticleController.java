package com.udteam.miristock.controller;

import com.udteam.miristock.dto.ArticleResponseDto;
import com.udteam.miristock.dto.ArticleRequestDto;
import com.udteam.miristock.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/article")
public class ArticleController {

    private Logger logger = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleService articleService;

    @GetMapping
    public List<ArticleResponseDto> findAll(){
        logger.info("articel 목록");
        return articleService.findAll();
    }

    @GetMapping("/{articleNo}")
    public ArticleResponseDto findOne(@PathVariable Long articleNo){
        logger.info("article 목록 한개");
        return articleService.findOne(articleNo);
    }

    @PostMapping
    public Long save(@RequestBody ArticleRequestDto articleRequestDto){
        logger.info("articleRequestDto : {}", articleRequestDto);
        return articleService.save(articleRequestDto);
    }

    @PutMapping
    public Long update(@RequestBody ArticleRequestDto articleRequestDto){
        logger.info("articleRequestDto : {}", articleRequestDto);
        return articleService.save(articleRequestDto);
    }

    @DeleteMapping
    public void delete(@RequestParam Long articleNo){
        logger.info("articleRequestDto : {}", articleNo);
        articleService.delete(articleNo);
    }

}
