package com.udteam.miristock.service;

import com.udteam.miristock.dto.CommentRequestDto;
import com.udteam.miristock.dto.CommentResponseDto;
import com.udteam.miristock.entity.CommentEntity;
import com.udteam.miristock.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<CommentResponseDto> findByArticleNo(Integer articleNo) {

        Optional<CommentEntity> CommentEntityList = Optional.ofNullable(commentRepository.findByArticle_ArticleNo(articleNo));
        return CommentEntityList.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional // 메서드 실행시 트랜잭션 시작, 정상종료되면 커밋, 에러발생시 종료
    public CommentResponseDto save(CommentRequestDto commentRequestDto) {
        return new CommentResponseDto(commentRepository.saveAndFlush(commentRequestDto.toEntity()));
    }

//    @Transactional
//    public CommentResponseDto update(CommentRequestDto commentRequestDto) {
//        return commentRepository.save(commentRequestDto.toEntity()).getCommentNo();
//    }

    @Transactional
    public void delete(Integer memberNo, Integer commentNo) {
        commentRepository.deleteByMemberNoAndCommentNo(memberNo, commentNo);
    }

}
