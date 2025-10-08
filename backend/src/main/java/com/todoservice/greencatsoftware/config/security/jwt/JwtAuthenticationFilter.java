package com.todoservice.greencatsoftware.config.security.jwt;

import com.todoservice.greencatsoftware.common.baseResponse.BaseResponseStatus;
import com.todoservice.greencatsoftware.common.exception.BaseException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter  extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final String AUTHORIZATION = "Authorization";
    private final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        log.info("[AUTH_DEBUG] JwtAuthenticationFilter started for request: {}", uri);
        //1. 요청 헤더에서 JWT 추출
        String token = resolveToken(request);
        log.info("[AUTH_DEBUG] Resolved token: {}", (token != null ? "Found" : "Not Found"));
        if ("/".equals(uri) || "/health".equals(uri)) {
            String ua = request.getHeader("User-Agent");
            String ip = request.getRemoteAddr();
            log.info("[TRACE_HEALTH] {} from IP={}, UA={}", uri, ip, ua);
        }

        //2. 토큰 유효성 검사
        try {
            if (token != null && jwtProvider.validToken(token)) {
                //3. 토큰에서 사용자 정보 추출
                log.info("[AUTH_DEBUG] Token is valid. Getting authentication...");
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("[AUTH_DEBUG] Authentication object set in SecurityContext.");
            }
        } catch (BaseException e) {
            log.error("[AUTH_DEBUG] Exception during authentication process: {}", e.getClass().getSimpleName(), e);
            if (e.getStatus() == BaseResponseStatus.TOKEN_EXPIRED) {
                request.setAttribute("exception", e);
            } else {
                throw e;
            }
        }

        //4. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        // 1. Authorization 헤더에서 토큰 확인
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }

        // 2. 헤더에 토큰이 없으면 쿠키에서 확인
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> "access_token".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        return null;
    }
}

