package com.udteam.miristock.dto;

import com.udteam.miristock.entity.ArticleEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ArticleResponseDto {
    private Long articleNo; // 글 번호
    private String memberNickname; // 작성자 닉네임
    private String articleTitle; // 제목
    private String articleContent; // 내용
    private LocalDateTime articleDate; // 작성날짜

    private List<CommentResponseDto> comments;

    public ArticleResponseDto(ArticleEntity entity){
        this.articleNo = entity.getArticleNo();
        this.memberNickname = entity.getMemberNickname();
        this.articleTitle = entity.getArticleTitle();
        this.articleContent = entity.getArticleContent();
        this.articleDate = entity.getArticleDate();
        this.comments = entity.getComments().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

}
