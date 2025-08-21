package com.ojo.mullyuojo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component // 자동 Bean 등록
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtTokenValidator jwtTokenValidator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        log.info("[Global JWT Filter] Incoming request path: {}", path);

        // 공개 경로 예외 처리
        List<String> publicPaths = List.of("/auth/login", "/auth/signup");
        if (publicPaths.stream().anyMatch(path::contains)) {
            log.info(">>> [JWT GlobalFilter] Public path, bypass auth");
            return chain.filter(exchange); // 토큰 검사 없이 통과
        }

        // JWT 추출
        String token = resolveToken(request);
        if (token != null && jwtTokenValidator.validateToken(token)) {
            String userId = jwtTokenValidator.getUserId(token);
            String userRole = jwtTokenValidator.getUserRole(token);
            log.info("[Global JWT Filter] Authenticated user: {}, adding X-User-Id header", userId);

            // 사용자 ID를 다음 서비스로 전달
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-USER-ID", userId)
                    .header("X-USER-ROLE", userRole)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        } else {
            log.warn("[Global JWT Filter] JWT Token is invalid or missing for {}", path);
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    private String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE; // Security 필터보다 앞서 실행

    }
}