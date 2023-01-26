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
    TokenService tokenservice;
    MemberRepository memberRepository;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = HeaderUtil.getAccessToken((HttpServletRequest) request);
        log.info("Filter token = {}", token);
        if (token!=null && tokenservice.verifyToken(token)){
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(memberRepository.findByMemberEmail(tokenservice.getUid(token))));
        }
        chain.doFilter(request,response);
    }

    public Authentication getAuthentication(MemberEntity member){
        return new UsernamePasswordAuthenticationToken(member.getMemberEmail(), "", Arrays.asList(new SimpleGrantedAuthority("ROLE_MEMBER")));
    }
}
