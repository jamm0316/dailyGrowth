package com.todoservice.greencatsoftware.config.security.service;

import com.todoservice.greencatsoftware.domain.member.domain.entity.Member;
import com.todoservice.greencatsoftware.domain.member.domain.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService  implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("Attempting to load user by ID: {}", userId);
        return memberRepository.findById(Long.parseLong(userId))
                .map(this::createUserDetails)
                .orElseThrow(() -> {
                    log.error("User not found for ID: {}", userId);
                    return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
                });
    }

    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getId().toString())
                .password(member.getPassword() != null ? member.getPassword() : "")  //소셜로그인 유저면 빈 비밀번호 가능
                .authorities("ROLE_USER")  //기본 권한 부여
                .build();
    }
}
