package com.todoservice.greencatsoftware.domain.auth.infrastructure.oauth.kakao.dto;

import com.todoservice.greencatsoftware.common.baseResponse.BaseResponseStatus;
import com.todoservice.greencatsoftware.common.exception.BaseException;
import com.todoservice.greencatsoftware.config.OAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient {
    private final RestTemplate restTemplate;
    private final OAuthProperties oAuth2Properties;
//    @Value("${oauth2.providers.kakao.rest-api-key}")
//    private String clientId;
//
//    @Value("${oauth2.providers.kakao.redirect-uri}")
//    private String redirectUri;

    public String requestAccessToken(String code) {
        OAuthProperties.Provider kakaoProvider = oAuth2Properties.getProviders().get("kakao");
        if (kakaoProvider == null) {
            throw new BaseException(BaseResponseStatus.UNSUPPORTED_PROVIDER);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProvider.getRestApiKey());
        params.add("redirect_uri", kakaoProvider.getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<KakaoTokenResponseDto> response = restTemplate.postForEntity(
                    "https://kauth.kakao.com/oauth/token",
                    request,
                    KakaoTokenResponseDto.class
            );

            return response.getBody().getAccessToken();

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST &&
                    e.getResponseBodyAsString().contains("KOE303")) {
                throw new BaseException(BaseResponseStatus.KAKAO_REDIRECT_MISMATCH);
            }
            throw new BaseException(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public KakaoUserInfoDto requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfoDto> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                KakaoUserInfoDto.class
        );

        return response.getBody();
    }
}
