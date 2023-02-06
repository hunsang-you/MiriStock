package com.udteam.miristock.service;

import com.udteam.miristock.dto.MemberStockDto;
import com.udteam.miristock.entity.LimitPriceOrderEntity;
import com.udteam.miristock.entity.MemberStockEntity;
import com.udteam.miristock.repository.MemberStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberStockService {

    private final MemberStockRepository memberStockRepository;

    // 회원 보유 주식 목록 출력하기
    public List<MemberStockDto> findAll(Integer memberNo) {
        List<MemberStockEntity> memberStockDtoList = memberStockRepository.findAllByMemberNo(memberNo);
        return memberStockDtoList.stream()
                .map(MemberStockDto::new)
                .collect(Collectors.toList());
    }

    // 회원 보유 주식 등록하기
    @Transactional
    public MemberStockDto save(MemberStockDto memberStockDto) {
        return new MemberStockDto(memberStockRepository.save(memberStockDto.toEntity()));
    }

    // 회원 보유 주식 삭제하기
    @Transactional
    public void delete(Integer memberNo, String stockCode) {
        memberStockRepository.deleteByMemberNoAndStockCode(memberNo, stockCode);
    }

}
