package com.udteam.miristock.dto;

import com.udteam.miristock.entity.ArticleEntity;
import com.udteam.miristock.entity.CommentEntity;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {

    private Integer commentNo; // 댓글 번호
    private Integer articleNo;
    private String memberNickname; // 작성자 닉네임
    private String commentContent; // 내용
    private LocalDateTime commentDate; // 작성시간


    public CommentEntity toEntity() {
        return CommentEntity.builder()
                .commentNo(commentNo)
                .article(ArticleEntity.builder().articleNo(articleNo).build())
                .memberNickname(memberNickname)
                .commentContent(commentContent)
                .commentDate(commentDate)
                .build();
    }

}
