package com.udteam.miristock.controller;

import com.udteam.miristock.dto.MemberDto;
import com.udteam.miristock.service.MemberService;
import com.udteam.miristock.util.HeaderUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 정보")
public class MemberController {
    private final MemberService memberservice;

    @GetMapping
    @Operation(summary = "회원 목록 출력", description = "서비스에 가입된 회원 목록을 출력한다.", tags = { "Member" })
    public ResponseEntity<List<MemberDto>> selectAllMember(){
        return ResponseEntity.ok().body(memberservice.selectAllMember());
    }

    @DeleteMapping
    @Operation(summary = "회원 탈퇴", description = "서비스에 가입된 회원 정보를 삭제한다.", tags = { "Member" })
    public ResponseEntity<String> deleteMember(@RequestHeader String Authorization){
        log.info("회원 삭제 요청됨");
        String userDetails = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = HeaderUtil.getAccessTokenString(Authorization);
        log.info("유저 디테일 = {}",userDetails);
        if (memberservice.deleteMember(token) != 0){
            // 회원삭제 성공
            return ResponseEntity.status(HttpStatus.OK).body("Delete Member Success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete Member Fail");
    }

    @PutMapping("/nickname")
    @Operation(summary = "회원 닉네임 수정", description = "서비스에 가입된 회원 닉네임 정보를 수정한다.", tags = { "Member" })
    public ResponseEntity<MemberDto> updateMember(@RequestHeader String Authorization, @RequestParam String nickname){
        String userDetails = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = HeaderUtil.getAccessTokenString(Authorization);
        log.info("유저 디테일 = {}",userDetails);
        if(memberservice.selectOneMember(token)== null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok().body(memberservice.updateMember(token,nickname));
    }

    @PostMapping("/nicknamecheck")
    @Operation(summary = "회원 닉네임 출력", description = "서비스에 가입된 회원 닉네임 정보를 출력한다.", tags = { "Member" })
    public ResponseEntity<?> selectAllMember(@RequestBody MemberDto memberDto){
        log.info("유저 닉네임 체크 호출됨 : {} " , memberDto);
        MemberDto getMember = memberservice.selectOnMemberByEmail(memberDto);
        if(getMember == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 정보를 찾을 수 없습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(getMember);
    }
}
