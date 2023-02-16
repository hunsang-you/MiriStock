package com.udteam.miristock.service.auth;

import com.udteam.miristock.util.RedisUtil;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final RedisUtil redisUtil;

    private String secretKey = "token-secret-key";
    public static long accessPeriod = 1000L * 60L * 60L * 24L * 2L;
    public static long refreshPeriod = 1000L * 60L * 60L * 24L * 30L;


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateToken(String email, String role, String nickname, String tokentype) {
        log.info("gen ROLE = {} , nickname =  {}",role,nickname);
        Claims claims = Jwts.claims()
                .setSubject(email);
        claims.put("role", role);
        claims.put("nickname", nickname);
        Date now = new Date();
        log.info("Expire Time = {}",new Date(now.getTime()+ (tokentype.equals("ACCESS") ? accessPeriod : refreshPeriod)));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(now.getTime() + (tokentype.equals("ACCESS") ? accessPeriod : refreshPeriod)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // true 일 경우 토큰 유효
    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        }catch(Exception e){
            e.printStackTrace();
            log.error("Access Token Expired");
        }
        return false;
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getPayload(String token, String className) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get(className,String.class);
    }

    public boolean getExpiredTokenClaims(String token) throws ExpiredJwtException{
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Refresh token");
            return true;
        }
        return false;
    }
}
