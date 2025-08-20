package com.ojo.mullyuojo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity // WebFlux Security 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    // JwtAuthenticationFilter는 GatewayFilterFactory로 등록되므로 직접 주입받지 않음
    // private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // CSRF 비활성화
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable) // HTTP Basic 비활성화
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable) // Form Login 비활성화
                // 세션 사용 안함 (STATELESS)
                .authorizeExchange(exchanges -> exchanges
                        // 로그인, 회원가입 API는 모든 요청 허용
                        .pathMatchers("/auth/login", "/auth/signin").permitAll()
                        //.pathMatchers("/**").permitAll()   // 임시로 모든 요청 허용

                        // 그 외 모든 요청은 인증 필요
                        //.anyExchange().authenticated()
                        .anyExchange().permitAll()
                );
                // WebFlux Security는 필터 체인에 직접 필터를 추가하는 방식이 다름
                // JwtAuthenticationFilter는 GatewayFilterFactory로 등록되어 라우트 설정에서 적용됨

        return http.build();
    }
}
