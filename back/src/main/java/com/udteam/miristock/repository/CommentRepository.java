package com.udteam.miristock.repository;

import com.udteam.miristock.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    CommentEntity findByArticle_ArticleNo (Long articleNo);
}

