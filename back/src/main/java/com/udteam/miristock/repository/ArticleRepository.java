package com.udteam.miristock.repository;

import com.udteam.miristock.dto.ArticleResponseDto;
import com.udteam.miristock.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer> {

     int deleteByMemberNoAndArticleNo(Integer memberNo, Integer articleNo);

     List<ArticleEntity> findByArticleTitleContainingOrArticleContentContaining(String keyword1, String keyword2);
}
