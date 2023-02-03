package com.udteam.miristock.service.auth;

import com.udteam.miristock.dto.auth.OAuth2Attribute;
import com.udteam.miristock.entity.MemberEntity;
import com.udteam.miristock.entity.Role;
import com.udteam.miristock.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import java.lang.reflect.Member;
import java.util.Collections;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2Service 객체 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // userRequest에 있는 access Token으로 정보 얻기
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        //	userRequest에 있는 registrationId ( security에서 제공하는 구분값 ) 과 userNameAttributeName ( 반환해주는 JSON값에서 원하는 파트만 필터링 )을 가져온다
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        log.info("registrationId = {}", registrationId);
        // naver는 response, kakao는 kakao_account 안에 필요한 정보가 들어가 있으니 해당 내용을 application-oauth.yml에 미리 설정해둠
        log.info("userNameAttributeName = {}", userNameAttributeName);

        // 얻어온 값들로 oAuth2Attribute 객체 생성 ( oAuth2User.getAttributes() 에는 반환받은 JSON 값이 들어가있음 )
        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 만들어진 객체를 map 형식으로 변환 후 OAuth2User 기본객체인 DefaultOAuth2User 생성후 리턴 ( 권한, 유저 데이터, nameattributeKey ( 사용자 식별을 하기위한 키 ))
        Map<String,Object> memberAttribute = oAuth2Attribute.convertToMap();
        log.info("login email = {}", (String) memberAttribute.get("email"));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER")),
                memberAttribute, "email");
    }
}
