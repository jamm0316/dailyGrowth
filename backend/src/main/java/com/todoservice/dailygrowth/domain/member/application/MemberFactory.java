package com.todoservice.dailygrowth.domain.member.application;

import com.todoservice.dailygrowth.domain.member.domain.entity.Member;
import com.todoservice.dailygrowth.domain.member.presentation.dto.MemberCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class MemberFactory {
    public Member createMember(MemberCreateRequest request) {
        return Member.create(
                request.email(),
                request.provider(),
                request.providerId(),
                request.password(),
                request.profileImageUrl(),
                request.name());
    }
}
