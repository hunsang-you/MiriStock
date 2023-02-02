package com.udteam.miristock.service;

import com.udteam.miristock.dto.MemberAssetDto;
import com.udteam.miristock.repository.MemberAssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberAssetService {
    private final MemberAssetRepository memberAssetRepository;


    public MemberAssetDto selectMemberAsset(Integer id){
        return MemberAssetDto.of(memberAssetRepository.findById(id).get());
    }

}
