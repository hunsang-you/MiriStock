package com.udteam.miristock.config;

import com.udteam.miristock.repository.MemberRepository;
import com.udteam.miristock.repository.RedisRepository;
import com.udteam.miristock.service.auth.CustomLogoutSuccessHandler;
import com.udteam.miristock.service.auth.CustomOAuth2UserService;
import com.udteam.miristock.service.auth.OAuth2SuccessHandler;
import com.udteam.miristock.service.auth.TokenService;
import com.udteam.miristock.util.CookieUtil;
import com.udteam.miristock.util.JwtAuthenticationFilter;
import com.udteam.miristock.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final TokenService tokenservice;
    private final MemberRepository memberRepository;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final RedisUtil redisUtil;
    private final RedisRepository redisRepository;
    private final CookieUtil cookieUtil;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception{
        http
                .httpBasic().disable()
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl("/member/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .logoutSuccessUrl("/")
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(tokenservice,redisUtil,cookieUtil,redisRepository), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login()
                .successHandler(oAuth2SuccessHandler)
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        return http.build();

    }


}
