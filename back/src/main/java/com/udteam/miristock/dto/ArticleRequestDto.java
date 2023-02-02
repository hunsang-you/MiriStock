package com.udteam.miristock.dto;


import com.udteam.miristock.entity.ArticleEntity;
import lombok.*;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequestDto {

    private Integer articleNo; // 글 번호
    private String memberNickname; // 작성자 닉네임
    private String articleTitle; // 제목
    private String articleContent; // 내용

    public ArticleEntity toEntity(){
        return ArticleEntity.builder()
                .articleNo(articleNo)
                .memberNickname(memberNickname)
                .articleTitle(articleTitle)
                .articleContent(articleContent)
                .build();
    }

}
