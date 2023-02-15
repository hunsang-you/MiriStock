package com.udteam.miristock.entity;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "article")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_no")
    private Integer articleNo; // PK

    @Column(name = "member_nickname")
    private String memberNickname; // 회원 닉네임

    @Column(name = "member_no")
    private Integer memberNo;

    @Column(name = "article_title")
    private String articleTitle; // 제목

    @Column(name = "article_content")
    private String articleContent; // 내용

    @CreationTimestamp
    @Column(name = "article_create_date", updatable = false)
    private LocalDateTime articleCreateDate; // 작성시간

    @UpdateTimestamp
    @Column(name = "article_modify_date", insertable = false)
    private LocalDateTime articleModifyDate; // 수정시간

    @Column(name = "article_heart_count", scale = 0)
    @ColumnDefault("0")
    private Integer articleHeartCount;

    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("commentNo asc") // 댓글 정렬
    private List<CommentEntity> comments;


}
