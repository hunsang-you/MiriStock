package com.udteam.miristock.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentNo;

    private String commentContent;

    private String memberNickname;

    @Column(insertable = false, updatable = false)
    private LocalDateTime commentDate; // 작성시간

    @ManyToOne
    @JoinColumn(name = "article_no")
    private ArticleEntity article; // 글 번호

    @Builder
    public CommentEntity (Long commentNo, String commentContent, String memberNickname, LocalDateTime commentDate, ArticleEntity article) {
        this.commentNo = commentNo;
        this.commentContent = commentContent;
        this.memberNickname = memberNickname;
        this.commentDate = commentDate;
        this.article = article;
    }

}
