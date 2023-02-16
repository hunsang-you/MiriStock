package com.udteam.miristock.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class CookieUtil {

    public ResponseCookie getCookie(String refreshtoken, long time) {
        return ResponseCookie.from("refreshtoken", refreshtoken)
                .maxAge(time)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
    }

    public String getRefreshTokenCookie(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            log.info("Cookie name = {}, Cookie Value = {}", cookie.getName(),cookie.getValue());
            if (cookie.getName().equals("refreshtoken")) {
                return cookie.getValue();
            }
        }
        throw new JwtException("RefreshToken is invaild");
    }

}
