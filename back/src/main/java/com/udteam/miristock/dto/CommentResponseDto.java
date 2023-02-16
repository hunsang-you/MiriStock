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
    private Integer articleNo; // 글 번호
    private Integer memberNo;
    private String memberNickname;
    private LocalDateTime commentCreateDate; // 작성시간
    private LocalDateTime commentModifyDate;

    public CommentResponseDto(CommentEntity entity){
        this.commentNo = entity.getCommentNo();
        this.commentContent = entity.getCommentContent();
        this.memberNickname = entity.getMemberNickname();
        this.memberNo = entity.getMemberNo();
        this.articleNo = entity.getArticle().getArticleNo();
        this.commentCreateDate = entity.getCommentCreateDate();
        this.commentModifyDate = entity.getCommentModifyDate();
    }

}
