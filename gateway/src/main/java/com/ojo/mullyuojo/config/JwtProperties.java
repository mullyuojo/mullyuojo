package com.ojo.mullyuojo.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * JWT 관련 설정을 application.yml에서 바인딩하는 클래스
 * @param issuer 토큰 발급자
 * @param secretKey 토큰 서명에 사용될 비밀 키
 * @param accessTokenExpirationMilisec 액세스 토큰 만료 시간 (시간 단위)
 * @param refreshTokenExpirationDays 리프레시 토큰 만료 시간 (일 단위)
 */
@ConfigurationProperties(prefix = "jwt")
@Validated
public record JwtProperties(
        @NotBlank
        String issuer,

        @NotBlank
        String secretKey,

        @Positive
        long accessTokenExpirationMilisec,

        @Positive
        long refreshTokenExpirationDays
) {
}