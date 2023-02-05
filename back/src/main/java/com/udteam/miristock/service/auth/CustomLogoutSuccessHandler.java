package com.udteam.miristock.service.auth;

import com.udteam.miristock.repository.RedisRepository;
import com.udteam.miristock.util.CookieUtil;
import com.udteam.miristock.util.HeaderUtil;
import com.udteam.miristock.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final RedisUtil redisUtil;
    private final RedisRepository redisRepository;
    private final TokenService tokenService;
    private final CookieUtil cookieUtil;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try {
            // 헤더에서 accesstoken 가져오기
            String accesstoken = HeaderUtil.getAccessToken(request);
            log.info("Logout access_token = {}",accesstoken);

            // Redis에 엑세스 토큰 블랙리스트 등록
            redisUtil.setDataExpire(accesstoken, "true", TokenService.accessPeriod);
        }catch(Exception e){
            e.printStackTrace();
        }

        // Cookie에서 RefreshToken을 가져와 Redis에서 RefreshToken 삭제
        String refreshtoken = cookieUtil.getRefreshTokenCookie(request);
        String email = tokenService.getEmail(refreshtoken);
        log.info("Log out Email = {}",email);
        redisUtil.delData(email);


        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("/");
    }
}
