package com.udteam.miristock.controller;

import com.udteam.miristock.dto.ArticleResponseDto;
import com.udteam.miristock.dto.ArticleRequestDto;
import com.udteam.miristock.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public List<ArticleResponseDto> findAll(){
        log.info("articel 목록");
        return articleService.findAll();
    }

    @GetMapping("/{articleNo}")
    public ArticleResponseDto findOne(@PathVariable Long articleNo){
        log.info("article 목록 한개");
        return articleService.findOne(articleNo);
    }

    @PostMapping
    public Long save(@RequestBody ArticleRequestDto articleRequestDto){
        log.info("articleRequestDto : {}", articleRequestDto);
        return articleService.save(articleRequestDto);
    }

    @PutMapping
    public Long update(@RequestBody ArticleRequestDto articleRequestDto){
        log.info("articleRequestDto : {}", articleRequestDto);
        return articleService.save(articleRequestDto);
    }

    @DeleteMapping
    public void delete(@RequestParam Long articleNo){
        log.info("articleRequestDto : {}", articleNo);
        articleService.delete(articleNo);
    }

}
