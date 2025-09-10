package com.todoservice.greencatsoftware.domain.member.domain.port;

import com.todoservice.greencatsoftware.domain.auth.domain.oauth.vo.OAuth2Provider;
import com.todoservice.greencatsoftware.domain.member.domain.entity.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> getMemberByIdOrThrow(Long id);
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByProviderAndProviderId(OAuth2Provider provider, String string);
}
