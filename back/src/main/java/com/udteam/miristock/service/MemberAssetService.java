package com.udteam.miristock.service;

import com.udteam.miristock.dto.MemberAssetDto;
import com.udteam.miristock.dto.RequestSimulationDto;
import com.udteam.miristock.entity.MemberAssetEntity;
import com.udteam.miristock.entity.MemberEntity;
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

//    public RequestSimulationDto updateMemberAssetTime(RequestSimulationDto requestSimulationDto) {
//        return RequestSimulationDto.memberEntityToSimulationResponseDto(
//                memberAssetRepository.save(
//                        MemberAssetEntity.builder()
//                                .member(MemberEntity.builder().memberNo(requestSimulationDto.getMemberNo()).build())
//                                .memberassetCurrentTime(requestSimulationDto.getMemberassetCurrentTime())
//                                .build()
//                ));
//    }

//        public MemberAssetDto updateMemberAssetTime(MemberAssetDto memberAssetDto) {
//        return MemberAssetDto.memberEntityToSimulationResponseDto(
//                memberAssetRepository.save(
//                        MemberAssetEntity.builder()
//                                .member(MemberEntity.builder().memberNo(memberAssetDto.getMemberNo()).build())
//                                .memberassetCurrentTime(memberAssetDto.getMemberassetCurrentTime())
//                                .build()
//                ));
//    }

}
