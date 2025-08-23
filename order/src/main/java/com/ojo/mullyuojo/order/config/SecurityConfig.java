package com.ojo.mullyuojo.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // REST API에서 CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/orders/**").permitAll() // 제품 API 전부 오픈
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults()) // 굳이 필요 없지만 기본값
                .build();
    }
}
