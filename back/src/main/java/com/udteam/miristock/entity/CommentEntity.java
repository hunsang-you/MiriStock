package com.udteam.miristock.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentNo;

    private Integer memberNo;

    private String commentContent;

    private String memberNickname;

    @CreationTimestamp
    @Column(name = "comment_create_date", updatable = false)
    private LocalDateTime commentCreateDate; // 작성시간

    @UpdateTimestamp
    @Column(name = "comment_modify_date")
    private LocalDateTime commentModifyDate;

    @ManyToOne
    @JoinColumn(name = "article_no")
    private ArticleEntity article; // 글 번호

}
