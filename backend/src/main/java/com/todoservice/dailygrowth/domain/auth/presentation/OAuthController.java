package com.todoservice.dailygrowth.domain.auth.presentation;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponse;
import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.util.CookieUtil;
import com.todoservice.dailygrowth.common.util.FingerprintUtil;
import com.todoservice.dailygrowth.config.security.token.TokenResponse;
import com.todoservice.dailygrowth.domain.auth.application.AuthService;
import com.todoservice.dailygrowth.domain.auth.domain.oauth.port.OAuthService;
import com.todoservice.dailygrowth.domain.auth.domain.oauth.vo.OAuthUserInfo;
import com.todoservice.dailygrowth.domain.auth.domain.oauth.vo.UserProfileResponse;
import com.todoservice.dailygrowth.domain.member.application.MemberService;
import com.todoservice.dailygrowth.domain.member.domain.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
    private final MemberService memberService;

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

        response.sendRedirect(redirectBaseUrl + "/oauth/callback");
    }

    @GetMapping("/me")
    public BaseResponse<UserProfileResponse> me(@AuthenticationPrincipal User user) {
        if (user == null) {
            return new BaseResponse<>(BaseResponseStatus.ACCESS_TOKEN_IS_NULL);
        }
        Member memberByIdOrThrow = memberService.getMemberByIdOrThrow(Long.parseLong(user.getUsername()));
        return new BaseResponse<>(UserProfileResponse.from(memberByIdOrThrow));
    }
}

