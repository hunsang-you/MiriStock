package com.udteam.miristock.service;

import com.udteam.miristock.dto.ArticleResponseDto;
import com.udteam.miristock.dto.ArticleRequestDto;
import com.udteam.miristock.entity.ArticleEntity;
import com.udteam.miristock.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<ArticleResponseDto> findAll() {
        List<ArticleEntity> articleEntityList = articleRepository.findAll();
        return articleEntityList.stream()
                .map(ArticleResponseDto::new)
                .collect(Collectors.toList());
    }

    public ArticleResponseDto findOne(Long articleNO) {
        return new ArticleResponseDto(articleRepository.findById(articleNO).get());
    }

    @Transactional // 메서드 실행시 트랜잭션 시작, 정상종료되면 커밋, 에러발생시 종료
    public Long save(ArticleRequestDto articleRequestDto) {
        return articleRepository.save(articleRequestDto.toEntity()).getArticleNo();
    }

    @Transactional
    public Long update(ArticleRequestDto articleRequestDto) {
        return articleRepository.save(articleRequestDto.toEntity()).getArticleNo();
    }

    @Transactional
    public void delete(Long articleNo) {
        articleRepository.delete(ArticleEntity.builder().articleNo(articleNo).build());
    }

}

