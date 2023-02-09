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

    public MemberAssetDto updateMemberAsset(MemberAssetDto memberAssetDto) {
        MemberAssetEntity result = memberAssetRepository.save(MemberAssetEntity.builder()
                        .memberassetNo(memberAssetDto.getMemberassetNo())
                        .member(MemberEntity.builder().memberNo(memberAssetDto.getMemberNo()).build())
                        .memberassetTotalAsset(memberAssetDto.getMemberassetTotalAsset())
                        .memberassetAvailableAsset(memberAssetDto.getMemberassetAvailableAsset())
                        .memberassetStockAsset(memberAssetDto.getMemberassetStockAsset())
                        .build());
        return MemberAssetDto.of(result);
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
