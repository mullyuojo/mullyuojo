package com.ojo.mullyuojo.config;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final String issuer;
    private final String secretKey;
    private final long accessTokenExpirationMilisec;
    private final long refreshTokenExpirationDays ;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.issuer = jwtProperties.issuer();
        this.secretKey = jwtProperties.secretKey();
        this.accessTokenExpirationMilisec = jwtProperties.accessTokenExpirationMilisec();
        this.refreshTokenExpirationDays = jwtProperties.refreshTokenExpirationDays();
    }


    public String createAccessToken(String user_id) {

        return Jwts.builder()
                // 사용자 ID를 클레임으로 설정
                .claim("user_id", user_id)
                .claim("role", "ADMIN")
                // JWT 발행자를 설정
                .issuer(issuer)
                // JWT 발행 시간을 현재 시간으로 설정
                .issuedAt(new Date(System.currentTimeMillis()))
                // JWT 만료 시간을 설정
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationMilisec))
                // SecretKey를 사용하여 HMAC-SHA512 알고리즘으로 서명
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, secretKey)
                // JWT 문자열로 컴팩트하게 변환
                .compact();
    }
}
