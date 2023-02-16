package com.udteam.miristock.repository;

import com.udteam.miristock.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    CommentEntity findByArticle_ArticleNo (Integer articleNo);

    int deleteByMemberNoAndCommentNo(Integer memberNo, Integer commentNo);

    int deleteByCommentNo(Integer commentNo);

}

