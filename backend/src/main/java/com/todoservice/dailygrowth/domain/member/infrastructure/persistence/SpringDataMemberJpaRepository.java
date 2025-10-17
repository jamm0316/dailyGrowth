package com.todoservice.dailygrowth.domain.member.infrastructure.persistence;

import com.todoservice.dailygrowth.domain.auth.domain.oauth.vo.OAuth2Provider;
import com.todoservice.dailygrowth.domain.member.domain.entity.Member;
import com.todoservice.dailygrowth.domain.member.domain.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface SpringDataMemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByProviderAndProviderId(OAuth2Provider provider, String string);

    @Repository
    @RequiredArgsConstructor
    class MemberJpaRepositoryImpl implements MemberRepository {
        private final SpringDataMemberJpaRepository jpa;

        @Override
        public Optional<Member> getMemberByIdOrThrow(Long id) {
            return jpa.findById(id);
        }

        @Override
        public Member save(Member member) {
            return jpa.save(member);
        }

        @Override
        public Optional<Member> findById(Long id) {
            return jpa.findById(id);
        }

        @Override
        public Optional<Member> findByProviderAndProviderId(OAuth2Provider provider, String string) {
            return jpa.findByProviderAndProviderId(provider, string);
        }
    }
}
