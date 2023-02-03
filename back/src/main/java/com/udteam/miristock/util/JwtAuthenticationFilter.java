package com.udteam.miristock.util;

import com.udteam.miristock.entity.MemberEntity;
import com.udteam.miristock.repository.MemberRepository;
import com.udteam.miristock.service.auth.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean{
    private final TokenService tokenservice;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = HeaderUtil.getAccessToken((HttpServletRequest) request);
        log.info("Filter token = {}", token);
        if (token!=null && tokenservice.verifyToken(token) && redisUtil.getData(tokenservice.getUid(token))==null){
            String email=tokenservice.getUid(token);
            MemberEntity m = memberRepository.findByMemberEmail(email);
            log.info("멤버이메일 = {}",m.getMemberEmail());
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(email));
        }
        chain.doFilter(request,response);
    }

    public Authentication getAuthentication(String email){
        log.info("저장할 정보",email);
        return new UsernamePasswordAuthenticationToken(email, "", Arrays.asList(new SimpleGrantedAuthority("ROLE_MEMBER")));
    }
}
