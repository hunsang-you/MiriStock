package com.udteam.miristock.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Builder
@Getter
public class OAuth2Attribute {
    private Map<String,Object> attributes;
    private String attributeKey;
    private String email;
    private String name;
    private String nickname;
    private String picture;
    private String provider;

    public static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes){
        switch(provider) {
            case "kakao":
                return ofKakao(attributeKey, attributes);
            case "naver":
                return ofNaver(attributeKey, attributes);
            default:
                throw new RuntimeException();
        }
    }

    private static OAuth2Attribute ofKakao(String attributeKey, Map<String,Object> attributes){
        Map<String,Object> kakaoAccount = (Map<String,Object>) attributes.get("kakao_account");
        Map<String,Object> kakaoProfile = (Map<String,Object>) kakaoAccount.get("profile");
        log.debug("{}",kakaoProfile.values());
        return OAuth2Attribute.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .picture((String) kakaoProfile.get("profile_image_url"))
                .nickname((String) kakaoProfile.get("nickname"))
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .provider("KAKAO")
                .build();
    }

    private static OAuth2Attribute ofNaver(String attributeKey,Map<String,Object> attributes){
        Map<String,Object> response = (Map<String,Object>) attributes.get("response");

        log.debug("{}",response.values());
        return OAuth2Attribute.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .nickname((String) response.get("nickname"))
                .attributes(response)
                .attributeKey(attributeKey)
                .provider("NAVER")
                .build();
    }

    public Map<String,Object> convertToMap(){
        Map<String,Object> map = new HashMap<>();

        map.put("id",attributeKey);
        map.put("key",attributeKey);
        map.put("name",name);
        map.put("nickname",nickname);
        map.put("email",email);
        map.put("picture",picture);
        map.put("provider",provider);

        return map;
    }

}
