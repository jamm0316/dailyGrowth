package com.todoservice.dailygrowth.domain.auth.application;

import com.todoservice.dailygrowth.config.security.token.TokenResponse;
import com.todoservice.dailygrowth.config.security.token.TokenService;
import com.todoservice.dailygrowth.domain.auth.domain.oauth.vo.OAuthUserInfo;
import com.todoservice.dailygrowth.domain.member.domain.entity.Member;
import com.todoservice.dailygrowth.domain.member.domain.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        String randomEmail = java.util.UUID.randomUUID().toString() + "@email.com";
        // 소셜 계정용 임의의 강한 비밀번호 생성(정책 충족: 대/소문자, 숫자, 특수문자, 길이)
        String rawPassword = "Soc1al@" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        //todo: 현재 테스트 용도, 추후에 바꿀 것
        Member member = Member.create(
                randomEmail, userInfo.getProvider(), userInfo.getProviderId(),
                rawPassword, userInfo.getProfileImageUrl(), userInfo.getName());
        return memberRepository.save(member);
    }
}
