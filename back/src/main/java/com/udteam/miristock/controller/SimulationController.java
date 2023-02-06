package com.udteam.miristock.controller;

import com.udteam.miristock.config.ErrorMessage;
import com.udteam.miristock.dto.MemberAssetDto;
import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.dto.RequestSimulationDto;
import com.udteam.miristock.dto.SimulEndDto;
import com.udteam.miristock.service.MemberAssetService;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.service.SimulationService;
import com.udteam.miristock.util.HeaderUtil;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/simulation")
@RequiredArgsConstructor
public class SimulationController {
    private final MemberAssetService memberAssetService;
    private final MemberService memberService;
    private final SimulationService simulationService;

    @GetMapping("/member/time")
    @ApiOperation(value = "회원 시뮬레이션 날짜 출력")
    public ResponseEntity<RequestSimulationDto> selectMemberAssetCurrentTime(@RequestHeader String Authorization) {
        log.info("회원 시뮬레이션 날짜 출력 호출됨 (/asset/member/time) ");
        String token= HeaderUtil.getAccessTokenString(Authorization);
        MemberDto m = memberService.selectOneMember(token);
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }else {
            MemberAssetDto result = memberAssetService.selectMemberAsset(m.getMemberNo());
            return ResponseEntity.ok().body(RequestSimulationDto.builder().memberNo(result.getMemberNo()).memberassetCurrentTime(result.getMemberassetCurrentTime()).build());
        }
    }

    // resultSimulation
    @PostMapping("/end")
        public ResponseEntity<SimulEndDto> resultSimulation(@RequestHeader String Authorization) {
        log.info("회원 시뮬레이션 종료 호출됨 ");
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.ok().body(simulationService.resultSimulation(m.getMemberNo()));
        }
    }
    @PutMapping("/member/time/{date}")
    public ResponseEntity<?> updateSimulationDate(@RequestHeader String Authorization, @PathVariable Integer date) {
        log.info("회원 시뮬레이션 날짜 변경 호출됨 -> 추가할 날짜  : {}", date);
        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));

        if (m == null){
            log.info(ErrorMessage.TOKEN_EXPIRE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            // simulationService
            return null;
//            return ResponseEntity.ok().body(memberAssetService.updateMemberAssetTime(requestSimulationDto));
        }
    }

//    @GetMapping("/hello/{date}")
//    public ResponseEntity<?> test(@PathVariable Integer date) {
//        simulationService.updateSimulationDate(1, date);
//        return null;
//    }


//    @PutMapping("/member/time")
//    public ResponseEntity<SimulationDto> update(@RequestHeader String Authorization, @RequestBody MemberAssetDto memberAssetDto) {
//        log.info("회원 시뮬레이션 날짜 변경 호출됨  : {}", memberAssetDto);
//        MemberDto m = memberService.selectOneMember(HeaderUtil.getAccessTokenString(Authorization));
//        if (m == null){
//            log.info(ErrorMessage.TOKEN_EXPIRE);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } else {
//            return ResponseEntity.ok().body(memberAssetService.updateMemberAssetTime(memberAssetDto));
//        }
//    }

}
