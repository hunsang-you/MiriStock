package com.udteam.miristock.service.auth;

import com.udteam.miristock.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
    private final long tokenPeriod = 1000L * 60L * 60L * 3L;
    private final long refreshPeriod = 1000L * 60L * 60L * 24L * 30L * 3L; // 3년


    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateToken(String email, String role,String nickname,String tokentype){
        // 여기에 필요한 데이터 추가 필요
        Claims claims = Jwts.claims()
                .setSubject(email);
        claims.put("role",role);
        claims.put("nickname",nickname);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(now.getTime() + (tokentype.equals("ACCESS") ? tokenPeriod : refreshPeriod)))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    public boolean verifyToken(String token){
        log.info("{}",getUid(token));
        try{
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getUid(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
}
