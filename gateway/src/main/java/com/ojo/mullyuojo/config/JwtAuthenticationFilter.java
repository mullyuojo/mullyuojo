package com.ojo.mullyuojo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component // 필터를 스프링 빈으로 등록
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenValidator jwtTokenValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 로그인, 회원가입 등의 경로는 필터를 타지 않도록 설정
        String path = request.getRequestURI();
        if (path.startsWith("/user-service/auth/") || path.startsWith("/user-service/users/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Request Header에서 JWT 토큰 추출
        String token = jwtTokenValidator.resolveToken(request);

        // 3. 토큰 유효성 검사
        if (token != null && jwtTokenValidator.validateToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가져와서 SecurityContext에 저장
            Authentication authentication = jwtTokenValidator.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 사용자 ID를 헤더에 추가하여 다음 서비스로 전달
            String userId = jwtTokenValidator.getUserId(token);
            MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
            mutableRequest.putHeader("X-User-Id", userId);
            log.info("Authenticated user: {}, adding X-User-Id header: {}", authentication.getName(), userId);

            filterChain.doFilter(mutableRequest, response);
        } else {
            // 토큰이 유효하지 않은 경우
            log.warn("JWT Token is invalid or not present. URI: {}", request.getRequestURI());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Unauthorized: Invalid Token");
        }
    }

    // 헤더를 수정하기 위한 커스텀 Wrapper 클래스
    private static class MutableHttpServletRequest extends HttpServletRequestWrapper {
        private final Map<String, String> customHeaders;

        public MutableHttpServletRequest(HttpServletRequest request) {
            super(request);
            this.customHeaders = new HashMap<>();
        }

        public void putHeader(String name, String value) {
            this.customHeaders.put(name, value);
        }

        @Override
        public String getHeader(String name) {
            String headerValue = customHeaders.get(name);
            if (headerValue != null) {
                return headerValue;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> names = Collections.list(super.getHeaderNames());
            names.addAll(customHeaders.keySet());
            return Collections.enumeration(names);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            List<String> values = Collections.list(super.getHeaders(name));
            if (customHeaders.containsKey(name)) {
                values.add(customHeaders.get(name));
            }
            return Collections.enumeration(values);
        }
    }
}
