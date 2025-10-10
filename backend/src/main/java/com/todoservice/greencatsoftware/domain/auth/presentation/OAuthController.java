package com.todoservice.greencatsoftware.domain.auth.presentation;

import com.todoservice.greencatsoftware.common.baseResponse.BaseResponse;
import com.todoservice.greencatsoftware.common.baseResponse.BaseResponseStatus;
import com.todoservice.greencatsoftware.common.util.CookieUtil;
import com.todoservice.greencatsoftware.common.util.FingerprintUtil;
import com.todoservice.greencatsoftware.config.security.token.TokenResponse;
import com.todoservice.greencatsoftware.domain.auth.application.AuthService;
import com.todoservice.greencatsoftware.domain.auth.domain.oauth.port.OAuthService;
import com.todoservice.greencatsoftware.domain.auth.domain.oauth.vo.OAuthUserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/oauth")
@RequiredArgsConstructor
@Log4j2
public class OAuthController {
    private final Map<String, OAuthService> oAuthServices;
    private final AuthService authService;

    @GetMapping("login/{provider}")
    public void redirectToProvider(@PathVariable String provider, HttpServletResponse response) throws IOException {
        OAuthService oAuthService = oAuthServices.get(provider.toLowerCase());
        String authUrl = oAuthService.buildAuthorizationUrl(provider);
        response.sendRedirect(authUrl);
    }

    @GetMapping("callback/{provider}")
    public void handleCallback(
            @PathVariable String provider,
            @RequestParam String code,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        OAuthService oAuthService = oAuthServices.get(provider.toLowerCase());
        String accessToken = oAuthService.getAccessToken(code);
        OAuthUserInfo userInfo = oAuthService.getUserInfo(accessToken);

        String uaHash = FingerprintUtil.uaHash(request.getHeader("User-Agent"));
        String ipPrefix = FingerprintUtil.ipPrefix(FingerprintUtil.extractClientIp(request));

        TokenResponse tokenResponse = authService.login(userInfo, uaHash, ipPrefix);
        CookieUtil.addTokenCookies(response, tokenResponse);

        String serverName = request.getServerName();
        String redirectBaseUrl = serverName.contains("localhost") || serverName.contains("127.0.0.1")
                ? "http://localhost:5173"
                : "https://dailygrowth.shop";

        response.sendRedirect(redirectBaseUrl + "/oauth/callback?provider=" + provider);
    }

    @GetMapping("/me")
    public BaseResponse<Object> me(Authentication authentication) {
        if (authentication == null) {
            return new BaseResponse<>(BaseResponseStatus.ACCESS_TOKEN_IS_NULL);
        }
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }
}
