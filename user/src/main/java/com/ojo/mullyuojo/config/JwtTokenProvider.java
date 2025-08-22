package com.ojo.mullyuojo.config;

import com.ojo.mullyuojo.domain.user.UserService;
import com.ojo.mullyuojo.domain.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final UserService userService;

    public JwtTokenProvider(JwtProperties jwtProperties, @Lazy UserService userService) {
        this.jwtProperties = jwtProperties;
        this.userService = userService;
    }


    private String issuer;
    private SecretKey key;
    private long accessTokenExpirationMilisec;
    private long refreshTokenExpirationDays;

    @PostConstruct
    protected void init() {
        this.issuer = jwtProperties.issuer();
        this.key = Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationMilisec = jwtProperties.accessTokenExpirationMilisec();
        this.refreshTokenExpirationDays = jwtProperties.refreshTokenExpirationDays();
    }


    public String createAccessToken(String user_id, UserRole.UserRoles role) {

        return Jwts.builder()
                .claim("user_id", user_id)
                .claim("role", "ROLE_" + role)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationMilisec))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    public String createRefreshToken(String user_id) {
        long expirationMillis = System.currentTimeMillis() + (refreshTokenExpirationDays * 24 * 60 * 60 * 1000);

        return Jwts.builder()
                .claim("user_id", user_id)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(expirationMillis))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Long user_id = Long.valueOf(this.getUserPk((token)));
        UserDetails userDetails = userService.loadUserById(user_id);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserPk(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get("user_id", String.class);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwtToken);
            return !claims.getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}