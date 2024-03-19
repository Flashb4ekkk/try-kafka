//package com.example.apigatewayservice;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Collections;
//@Component
//@Slf4j
//public class JwtRequestFilter extends OncePerRequestFilter {
//    @Autowired
//    private JwtTokenService JwtTokenService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        String email = null;
//        String jwt = null;
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            jwt = authHeader.substring(7);
//            try {
//                email = JwtTokenService.getEmail(jwt);
//                System.out.println(email);
//            } catch (ExpiredJwtException e) {
//                log.debug("Time of token expired");
//            }
//        }
//
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            Role role = JwtTokenService.getRole(jwt);
//            System.out.println(role);
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
//            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
//                    email,
//                    null,
//                    Collections.singletonList(authority)
//            );
//            SecurityContextHolder.getContext().setAuthentication(token);
//        }
//        filterChain.doFilter(request, response);
//    }
//}