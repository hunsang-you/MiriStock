package com.udteam.miristock.dto;

import com.udteam.miristock.entity.ArticleEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleResponseDto {
    private Integer articleNo; // 글 번호
    private Integer memberNo;
    private String memberNickname; // 작성자 닉네임
    private String articleTitle; // 제목
    private String articleContent; // 내용
    private LocalDateTime articleCreateDate; // 작성날짜
    private LocalDateTime articleModifyDate; // 수정날짜
    private Integer articleHeartCount;
    private List<CommentResponseDto> comments;


    @Builder
    public ArticleResponseDto(ArticleEntity entity){
        this.articleNo = entity.getArticleNo();
        this.memberNo = entity.getMemberNo();
        this.memberNickname = entity.getMemberNickname();
        this.articleTitle = entity.getArticleTitle();
        this.articleContent = entity.getArticleContent();
        this.articleCreateDate = entity.getArticleCreateDate();
        this.articleModifyDate = entity.getArticleModifyDate();
        this.articleHeartCount = entity.getArticleHeartCount();
        this.comments = entity.getComments().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

}
