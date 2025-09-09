package com.todoservice.greencatsoftware.domain.auth.infrastructure.oauth.kakao;

import com.todoservice.greencatsoftware.common.baseResponse.BaseResponseStatus;
import com.todoservice.greencatsoftware.common.exception.BaseException;
import com.todoservice.greencatsoftware.config.OAuthProperties;
import com.todoservice.greencatsoftware.domain.auth.domain.oauth.port.OAuthService;
import com.todoservice.greencatsoftware.domain.auth.domain.oauth.vo.OAuth2Provider;
import com.todoservice.greencatsoftware.domain.auth.infrastructure.oauth.kakao.dto.KakaoOAuthClient;
import com.todoservice.greencatsoftware.domain.auth.infrastructure.oauth.kakao.dto.KakaoUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService implements OAuthService {
    final private KakaoOAuthClient kakaoOAuthClient;
    final private OAuthProperties oAuth2Properties;

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.KAKAO;
    }

    @Override
    public String getAccessToken(String code) {
        return kakaoOAuthClient.requestAccessToken(code);
    }

    @Override
    public KakaoUserInfoDto getUserInfo(String accessToken) {
        return kakaoOAuthClient.requestUserInfo(accessToken);
    }

    @Override
    public String buildAuthorizationUrl(String providerName) {
        OAuthProperties.Provider providerConfig = oAuth2Properties.getProviders().get(providerName);
        if (providerConfig == null) {
            throw new BaseException(BaseResponseStatus.UNSUPPORTED_PROVIDER);
        }

        return UriComponentsBuilder
                .fromHttpUrl(OAuth2Provider.KAKAO.getAuthorizeUrl())
                .queryParam("response_type", "code")
                .queryParam("client_id", providerConfig.getRestApiKey())
                .queryParam("redirect_uri", providerConfig.getRedirectUri())
                .build(true)
                .toUriString();
    }
}
