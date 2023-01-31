package com.udteam.miristock.dto;

import com.udteam.miristock.entity.ArticleEntity;
import com.udteam.miristock.entity.CommentEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@ToString
public class CommentRequestDto {

    private Long commentNo; // 댓글 번호

    private Long articleNo;
    private String memberNickname; // 작성자 닉네임
    private String commentContent; // 내용
    private LocalDateTime commentDate; // 작성시간

    @Builder
    public CommentRequestDto(Long commentNo, Long articleNo, String memberNickname, String commentContent, LocalDateTime commentDate) {
        this.commentNo = commentNo;
        this.articleNo = articleNo;
        this.memberNickname = memberNickname;
        this.commentContent = commentContent;
        this.commentDate = commentDate;
    }

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
