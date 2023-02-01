package com.udteam.miristock.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

}
