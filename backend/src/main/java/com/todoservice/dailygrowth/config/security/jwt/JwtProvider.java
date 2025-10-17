package com.todoservice.dailygrowth.config.security.jwt;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.exception.BaseException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;

    public String generateToken (String userId) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + jwtProperties.getAccessTokenExpiration()), userId);
    }

    //JWT 토큰 생성 메서드
    public String makeToken(Date expiry, String userId) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(userId)
                .claim("id", userId)
                .signWith(Keys.hmacShaKeyFor(
                                Base64.getDecoder().decode(jwtProperties.getSecretKey())),
                        SignatureAlgorithm.HS256)
                .compact();
    }

    //JWT 토큰 유효성 검증 메서드
    public boolean validToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(
                            Base64.getDecoder().decode(jwtProperties.getSecretKey())))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | SignatureException e) {
            log.error("[AUTH_DEBUG] Invalid JWT Signature: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.TOKEN_INVALID_SIGNATURE);
        } catch (MalformedJwtException e) {
            log.error("[AUTH_DEBUG] Invalid JWT Token: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.TOKEN_MALFORMED);
        } catch (ExpiredJwtException e) {
            log.error("[AUTH_DEBUG] Expired JWT Token: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.error("[AUTH_DEBUG] Unsupported JWT Token: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.TOKEN_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            log.error("[AUTH_DEBUG] JWT claims si empty: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.TOKEN_ILLEGAL_ARGUMENT);
        }
    }

    //인증 객체 생성
    public Authentication getAuthentication(String token) {
        String userId = getUserIdFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }

    // 토큰에서 userId 추출
    public String getUserIdFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(
                            Base64.getDecoder().decode(jwtProperties.getSecretKey())))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            //토큰이 만료된 경우 예외에서 Claims를 가져온다.
            return e.getClaims().getSubject();
        }
    }
}
