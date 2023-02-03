package com.udteam.miristock.dto;

import com.udteam.miristock.entity.CommentEntity;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Integer commentNo;
    private String commentContent;
    private String memberNickname;
    private LocalDateTime commentDate; // 작성시간
    private Integer articleNo; // 글 번호

    public CommentResponseDto(CommentEntity entity){
        this.commentNo = entity.getCommentNo();
        this.commentContent = entity.getCommentContent();
        this.memberNickname = entity.getMemberNickname();
        this.commentDate = entity.getCommentDate();
        this.articleNo = entity.getArticle().getArticleNo();
    }

}
