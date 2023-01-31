package com.udteam.miristock.dto;


import com.udteam.miristock.entity.ArticleEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ArticleRequestDto {

    private Long articleNo; // 글 번호
    private String memberNickname; // 작성자 닉네임
    private String articleTitle; // 제목
    private String articleContent; // 내용

    @Builder
    public ArticleRequestDto(Long articleNo, String memberNickname, String articleTitle, String articleContent) {
        this.articleNo = articleNo;
        this.memberNickname = memberNickname;
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
    }

    public ArticleEntity toEntity(){
        return ArticleEntity.builder()
                .articleNo(articleNo)
                .memberNickname(memberNickname)
                .articleTitle(articleTitle)
                .articleContent(articleContent)
                .build();
    }

}
