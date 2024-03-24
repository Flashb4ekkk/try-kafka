package com.example.apigatewayservice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.accessToken.lifetime}")
    private Duration jwtAccessTokenLifetime;

    @Value("${jwt.refreshToken.lifetime}")
    private Duration jwtRefreshTokenLifetime;

    public String getEmail(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Role getRole(String token) {
        String roleString = getAllClaimsFromToken(token).get("role", String.class);
        return Role.valueOf(roleString);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    String generateJwt(UserDetails userDetails) {
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        Map<String, Object> claims = new HashMap<>();
        Role role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .findFirst()
                .orElse(Role.ROLE_USER);
        claims.put("role", role);

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtAccessTokenLifetime.toMillis());
        return Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}
