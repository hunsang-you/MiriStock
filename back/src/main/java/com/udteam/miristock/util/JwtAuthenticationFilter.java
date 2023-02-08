package com.udteam.miristock.util;

import com.udteam.miristock.entity.MemberEntity;
import com.udteam.miristock.entity.Role;
import com.udteam.miristock.repository.MemberRepository;
import com.udteam.miristock.repository.RedisRepository;
import com.udteam.miristock.service.auth.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokenservice;
    private final RedisUtil redisUtil;
    private final CookieUtil cookieUtil;
    private final RedisRepository redisRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = HeaderUtil.getAccessToken(request);
        String refreshtoken =null;
        log.info("Filter token = {}", token);
        try {
            // 엑세스 토큰 검증
            if (tokenservice.verifyToken(token)) {
                String email = tokenservice.getEmail(token);
                // 엑세스 토큰 블랙리스트 등록 여부 검증
                log.info(redisUtil.getData(token));
                if (redisUtil.getData(token) != null){
                    throw new JwtException("Blacklist JWT Token");
                }
                log.info("Member Email = {}", email);
                response.setHeader("Authorization", "Bearer " + token);
                Authentication auth = getAuthentication(email,tokenservice.getPayload(token,"role"));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }else{
                throw new JwtException("Access Expired");
            }
            // 엑세스 토큰이 블랙리스트 등록되있거나 만료되었을 경우 access token, refresh token 재발급
        }catch(Exception e){
            e.printStackTrace();
            log.info("JWT Token Reissue");
            try {
                // request의 Cookie에서 refresh token 추출
                refreshtoken = cookieUtil.getRefreshTokenCookie(request);
                String email=null;
                try {
                    email = tokenservice.getEmail(refreshtoken);
                }catch(Exception a){
                    throw new JwtException("RefreshToken Expired or Error");
                }
                // Redis를 통한 리프레쉬 토큰 검증
//                log.info("Redis Refresh check = {}",redisUtil.getData(email));
                if(redisUtil.getData(email)==null || tokenservice.getExpiredTokenClaims(refreshtoken)){
                    log.info ("Refreshtoken Expired");
                    throw new JwtException("RefreshToken Expired");
                }

                String nickname = tokenservice.getPayload(refreshtoken,"nickname");
                String role = tokenservice.getPayload(refreshtoken,"role");
                log.info("ROLE = {}" ,role);
                // 토큰에서 파싱한 데이터 기반으로 access token, refresh token 재발급
                String accesstoken = tokenservice.generateToken(email,role,nickname,"ACCESS");
                String newrefreshtoken = tokenservice.generateToken(email,role,nickname,"REFRESH");
                // 기존에 존재하던 refreshtoken 삭제
                redisRepository.deleteById(email);
                // 새로운 refreshtoken redis에 등록
                redisUtil.setDataExpire(email,newrefreshtoken,tokenservice.refreshPeriod);
                // 재발급한 access token, refresh token 응답 헤더에 넣은 후 SecurityContext에 적재
                ResponseCookie cookie = cookieUtil.getCookie(newrefreshtoken,tokenservice.refreshPeriod);
                response.setContentType("application/json;charset=UTF-8");
                response.setHeader("Authorization", "Bearer " + accesstoken);
                response.setHeader("Set-Cookie",cookie.toString());
                Authentication auth =getAuthentication(email,role);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }catch(JwtException j){
                // 리프레쉬 토큰도 없을 경우에는 재 로그인하라고 리턴을 해줘야하는데.. 어떻게 해야되지?
                log.debug("Refreshtoken invaild");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//                request.setAttribute("RefreshTokenInvalid",j.getMessage());
                j.printStackTrace();
            }
        }
        filterChain.doFilter(request,response);
    }

    public Authentication getAuthentication(String email, String role){
        log.info("Auth Email, Role = {} , {}",email,role);
        return new UsernamePasswordAuthenticationToken(email, "", Collections.singleton((new SimpleGrantedAuthority(role))));
    }
}
