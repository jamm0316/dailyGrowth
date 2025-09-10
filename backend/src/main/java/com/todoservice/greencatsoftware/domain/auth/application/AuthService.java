package com.todoservice.greencatsoftware.domain.auth.application;

import com.todoservice.greencatsoftware.config.security.token.TokenResponse;
import com.todoservice.greencatsoftware.config.security.token.TokenService;
import com.todoservice.greencatsoftware.domain.auth.domain.oauth.vo.OAuthUserInfo;
import com.todoservice.greencatsoftware.domain.member.domain.entity.Member;
import com.todoservice.greencatsoftware.domain.member.domain.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    final private MemberRepository memberRepository;
    private final TokenService tokenService;

    @Transactional
    public TokenResponse login (OAuthUserInfo userInfo, String uaHash, String ipPrefix) {
        Member member = memberRepository.findByProviderAndProviderId(
                        userInfo.getProvider(), userInfo.getProviderId())
                .orElseGet(() -> signIn(userInfo));

        return tokenService.generateTokenPair(member.getId().toString(), uaHash, ipPrefix);
    }

    private Member register(OAuthUserInfo userInfo) {
        Member member = memberRepository.findByProviderAndProviderId(
                        userInfo.getProvider(), userInfo.getProviderId())
                .orElseGet(() -> signIn(userInfo));

        return memberRepository.save(member);
    }

    public Member signIn(OAuthUserInfo userInfo) {
        String randomEmail = UUID.randomUUID().toString();

        //todo: 현재 테스트 용도, 추후에 바꿀 것
        Member member = Member.create(
                randomEmail + "@email.com", userInfo.getProvider(), userInfo.getProviderId(),
                "{noop}social_login_user", userInfo.getProfileImageUrl(), userInfo.getName());
        return memberRepository.save(member);
    }
}
