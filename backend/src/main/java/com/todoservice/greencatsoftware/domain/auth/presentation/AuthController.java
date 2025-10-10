package com.todoservice.greencatsoftware.domain.auth.presentation;

import com.todoservice.greencatsoftware.common.baseResponse.BaseResponse;
import com.todoservice.greencatsoftware.common.baseResponse.BaseResponseStatus;
import com.todoservice.greencatsoftware.common.util.CookieUtil;
import com.todoservice.greencatsoftware.common.util.FingerprintUtil;
import com.todoservice.greencatsoftware.config.security.token.TokenResponse;
import com.todoservice.greencatsoftware.config.security.token.TokenService;
import com.todoservice.greencatsoftware.domain.auth.application.AuthService;
import com.todoservice.greencatsoftware.domain.auth.domain.oauth.vo.OAuthUserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public BaseResponse<Object> login(
            @RequestBody OAuthUserInfo userInfo,
            HttpServletRequest request) {

        String uaHash = FingerprintUtil.uaHash(request.getHeader("User-Agent"));
        String ipPrefix = FingerprintUtil.ipPrefix(FingerprintUtil.extractClientIp(request));
        TokenResponse jwt = authService.login(userInfo, uaHash, ipPrefix);
        return new BaseResponse<>(jwt);
    }

    @PostMapping("/reissue")
    public BaseResponse<TokenResponse> reissue(
            @CookieValue("access_token") String accessToken,
            @CookieValue("refresh_token") String refreshToken,
            HttpServletRequest request,
            HttpServletResponse response) {
        String uaHash = FingerprintUtil.uaHash(request.getHeader("User-Agent"));
        String ipPrefix = FingerprintUtil.ipPrefix(FingerprintUtil.extractClientIp(request));

        TokenResponse newTokens = tokenService.reissueAccessToken(accessToken, refreshToken, uaHash, ipPrefix);

        CookieUtil.addRefreshTokenCookies(response, newTokens.getRefreshToken());
        return new BaseResponse<>(new TokenResponse(newTokens.getAccessToken(), null));
    }

    @PostMapping("/logout")
    public BaseResponse<Object> logout(
            @CookieValue String accessToken,
            @CookieValue String refreshToken,
            HttpServletResponse response) {
        tokenService.deleteRefreshToken(accessToken, refreshToken);
        CookieUtil.deleteCookie(response, "accessToken");
        CookieUtil.deleteCookie(response, "refreshToken");

        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }
}
