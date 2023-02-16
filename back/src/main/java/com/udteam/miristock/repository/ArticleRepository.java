package com.udteam.miristock.repository;

import com.udteam.miristock.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer> {

     int deleteByMemberNoAndArticleNo(Integer memberNo, Integer articleNo);

     List<ArticleEntity> findByArticleTitleContainingOrArticleContentContaining(String keyword1, String keyword2);

     int deleteByArticleNo(Integer articleNo);

     @Query(nativeQuery = true,
             value = " select * from article order by article_no desc limit 10 offset :index")
     List<ArticleEntity> findArticleList(@Param("index") Integer index);

}
