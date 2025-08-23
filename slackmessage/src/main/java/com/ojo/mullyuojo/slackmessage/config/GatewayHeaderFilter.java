<<<<<<< HEAD
package com.ojo.mullyuojo.slackmessage.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class GatewayHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 게이트웨이가 추가한 헤더들을 읽어옵니다.
        String userId = request.getHeader("X-USER-ID");
        String rolesString = request.getHeader("X-USER-ROLE"); //

        // userId와 rolesString 헤더가 모두 존재할 때만 인증 객체를 생성합니다.
        if (userId != null && !userId.isEmpty() && rolesString != null && !rolesString.isEmpty()) {

            // 쉼표로 구분된 역할 문자열로부터 GrantedAuthority 리스트를 생성합니다.
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(rolesString);

            // 헤더 정보만으로 Authentication 객체를 직접 생성합니다. (DB 조회 X)
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId, // principal은 이제 String 타입의 ID 입니다.
                    null,
                    authorities
            );

            // SecurityContext에 인증 정보를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
=======
package com.ojo.mullyuojo.slackmessage.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class GatewayHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 게이트웨이가 추가한 헤더들을 읽어옵니다.
        String userId = request.getHeader("X-USER-ID");
        String rolesString = request.getHeader("X-USER-ROLE"); //

        // userId와 rolesString 헤더가 모두 존재할 때만 인증 객체를 생성합니다.
        if (userId != null && !userId.isEmpty() && rolesString != null && !rolesString.isEmpty()) {

            // 쉼표로 구분된 역할 문자열로부터 GrantedAuthority 리스트를 생성합니다.
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(rolesString);

            // 헤더 정보만으로 Authentication 객체를 직접 생성합니다. (DB 조회 X)
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId, // principal은 이제 String 타입의 ID 입니다.
                    null,
                    authorities
            );

            // SecurityContext에 인증 정보를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
