package com.udteam.miristock.service.auth;

import com.udteam.miristock.entity.MemberEntity;
import com.udteam.miristock.entity.RefreshTokenEntity;
import com.udteam.miristock.entity.Role;
import com.udteam.miristock.repository.MemberRepository;
import com.udteam.miristock.repository.RefreshTokenRepository;
import com.udteam.miristock.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenService tokenservice;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        log.info("Principal에서 꺼낸 OAuth2User = {} ", oAuth2User);

        log.info("DB 등록 확인");
        MemberEntity member = memberRepository.findByMemberEmail((String) oAuth2User.getAttribute("email"));
        if (oAuth2User != null && member == null) {
            log.info("유저를 찾을 수 없습니다. 유저 정보를 등록합니다.");
            createMember(oAuth2User);
        }

        if (member != null && !member.getMemberProvider().equals(oAuth2User.getAttribute("provider"))) {
            log.info("다른 소셜에 등록된 회원입니다");
            getRedirectStrategy().sendRedirect(request, response, UriComponentsBuilder.fromUriString("/error")
                    .build().toUriString());
            return;
        }
        String nickname = memberRepository.findByMemberEmail((String) oAuth2User.getAttribute("email")).getMemberNickname();
        //log.info("refreshToken = {}",redisUtil.getData((String) oAuth2User.getAttribute("email")));
        if (redisUtil.getData((String) oAuth2User.getAttribute("email")) == null) {
            log.info("refresh token이 존재하지 않습니다. refresh token 생성");

            // Redis에 저장
            String refreshtoken=tokenservice.generateToken((String) oAuth2User.getAttribute("email"), "MEMBER",nickname, "REFRESH");
            redisUtil.setDataExpire((String) oAuth2User.getAttribute("email"),refreshtoken,1000L * 60L * 60L * 24L * 30L * 3L);
        }

        String accesstoken = tokenservice.generateToken(oAuth2User.getAttribute("email"), "MEMBER",nickname, "ACCESS");
        log.info("accecss_Token = {}", accesstoken);

        if (nickname == null){
            // 닉네임 설정 화면으로
            getRedirectStrategy().sendRedirect(request, response, UriComponentsBuilder.fromUriString("/")
                    .queryParam("accesstoken", accesstoken)
                    .build().toUriString());
        }else {
            // 메인으로
            getRedirectStrategy().sendRedirect(request, response, UriComponentsBuilder.fromUriString("/")
                    .queryParam("accesstoken", accesstoken)
                    .build().toUriString());
        }
    }


    private RefreshTokenEntity updatetoken(String email) {
        return refreshTokenRepository.save(RefreshTokenEntity.builder()
                .email(email)
                .build());
    }

    private MemberEntity createMember(OAuth2User oAuth2User) {
        return memberRepository.saveAndFlush(MemberEntity.builder()
                .memberEmail((String) oAuth2User.getAttribute("email"))
                .memberCurrentTime(20150101)
                .memberTotalasset(50000000L)
                .role(Role.MEMBER)
                .memberProvider(oAuth2User.getAttribute("provider"))
                .build());
    }
}
