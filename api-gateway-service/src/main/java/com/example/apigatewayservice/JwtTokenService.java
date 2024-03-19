//package com.example.apigatewayservice;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Service;
//
//
//@Service
//@Lazy
//public class JwtTokenService {
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    public String getEmail(String token) {
//        return getAllClaimsFromToken(token).getSubject();
//    }
//
//    public Role getRole(String token) {
//        String roleString = getAllClaimsFromToken(token).get("role", String.class);
//        return Role.valueOf(roleString);
//    }
//
//    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser()
//                .setSigningKey(secret)
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}
