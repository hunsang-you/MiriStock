package com.udteam.miristock.dto;

import com.udteam.miristock.entity.ArticleEntity;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCUDResponseDto {

    private Integer articleNo; // 글 번호
    private Integer memberNo;
    private String memberNickname; // 작성자 닉네임
    private String articleTitle; // 제목
    private String articleContent; // 내용
    private LocalDateTime articleCreateDate; // 작성날짜
    private LocalDateTime articleModifyDate; // 수정날짜
    private Integer articleHeartCount;

    @Builder
    public ArticleCUDResponseDto(ArticleEntity entity){
        this.articleNo = entity.getArticleNo();
        this.memberNo = entity.getMemberNo();
        this.memberNickname = entity.getMemberNickname();
        this.articleTitle = entity.getArticleTitle();
        this.articleContent = entity.getArticleContent();
        this.articleCreateDate = entity.getArticleCreateDate();
        this.articleModifyDate = entity.getArticleModifyDate();
        this.articleHeartCount = entity.getArticleHeartCount();
    }
}
