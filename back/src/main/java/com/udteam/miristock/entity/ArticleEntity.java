package com.udteam.miristock.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "article")
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleNo; // PK

    @Column(columnDefinition = "TEXT", nullable = false)
    private String memberNickname; // 회원 닉네임

    private String articleTitle; // 제목

    private String articleContent; // 내용

    @Column(insertable = false, updatable = false)
    private LocalDateTime articleDate; // 작성시간

    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("commentNo asc") // 댓글 정렬
    private List<CommentEntity> comments;


    @Builder
    public ArticleEntity (Long articleNo, String memberNickname, String articleTitle, String articleContent, LocalDateTime articleDate, List<CommentEntity> comments) {
        this.articleNo = articleNo;
        this.memberNickname = memberNickname;
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
        this.articleDate = articleDate;
        this.comments = comments;
    }

}
