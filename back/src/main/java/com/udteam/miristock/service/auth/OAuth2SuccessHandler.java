package com.udteam.miristock.service.auth;

import com.udteam.miristock.entity.MemberEntity;
import com.udteam.miristock.entity.RefreshTokenEntity;
import com.udteam.miristock.entity.Role;
import com.udteam.miristock.repository.MemberRepository;
import com.udteam.miristock.repository.RefreshTokenRepository;
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
        if(member!=null && !member.getMemberProvider().equals(oAuth2User.getAttribute("provider"))){
            log.info("다른 소셜에 등록된 회원입니다");
            getRedirectStrategy().sendRedirect(request, response, UriComponentsBuilder.fromUriString("/error")
                    .build().toUriString());
            return;
        }
        log.info("토큰 발행 시작");


        if (refreshTokenRepository.findByEmail((String) oAuth2User.getAttribute("email")) == null) {
            log.info("refresh token이 존재하지 않습니다. refresh token 생성");

            // 테이블 생성 고려해 봐야할듯
            updatetoken((String) oAuth2User.getAttribute("email"), tokenservice.generateToken(oAuth2User.getAttribute("email"), "MEMBER", "REFRESH"));
        }

        String accesstoken = tokenservice.generateToken(oAuth2User.getAttribute("email"), "MEMBER", "ACCESS");
        log.info("accecss_Token = {}", accesstoken);

        // if문으로 만약 닉네임이 null 이면 닉네임 설정 페이지로 아니면 로그인 처리 후 메인으로로
       getRedirectStrategy().sendRedirect(request, response, UriComponentsBuilder.fromUriString("/")
                .queryParam("accesstoken", accesstoken)
                .build().toUriString());
    }

    private RefreshTokenEntity updatetoken(String email, String token) {
        return refreshTokenRepository.save(RefreshTokenEntity.builder()
                .email(email)
                .refreshToken(token)
                .build());
    }

    private MemberEntity createMember(OAuth2User oAuth2User) {
        return memberRepository.saveAndFlush(MemberEntity.builder()
                .memberEmail((String) oAuth2User.getAttribute("email"))
                .memberCurrentTime(20150101)
                .role(Role.MEMBER)
                .memberProvider(oAuth2User.getAttribute("provider"))
                .build());
    }
}
