package com.todoservice.dailygrowth.domain.auth.infrastructure.oauth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.todoservice.dailygrowth.domain.auth.domain.oauth.vo.OAuth2Provider;
import com.todoservice.dailygrowth.domain.auth.domain.oauth.vo.OAuthUserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto extends OAuthUserInfo {

    @JsonCreator
    public KakaoUserInfoDto(
            @JsonProperty("id") Long id,
            @JsonProperty("kakao_account") KakaoAccount kakaoAccount
    ) {
        super(
                id.toString(),
                kakaoAccount.getProfile().nickname,
                null,
                kakaoAccount.getProfile().profileImageUrl
        );
        this.id = id;
        this.kakaoAccount = kakaoAccount;
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.KAKAO;
    }

    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    public static class KakaoAccount {
        @JsonProperty("profile")
        private Profile profile;
    }

    @Getter
    @NoArgsConstructor
    public static class Profile {
        @JsonProperty("nickname")
        private String nickname;

        @JsonProperty("thumbnail_image_url")
        private String thumbnailImageUrl;

        @JsonProperty("profile_image_url")
        private String profileImageUrl;
    }
}
