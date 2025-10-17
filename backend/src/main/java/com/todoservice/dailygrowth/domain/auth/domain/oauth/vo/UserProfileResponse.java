package com.todoservice.dailygrowth.domain.auth.domain.oauth.vo;

import com.todoservice.dailygrowth.domain.member.domain.entity.Member;

public record UserProfileResponse (
        String name,
        String email,
        String profileImageUrl,
        String providerId
) {
    public static UserProfileResponse from(Member member) {
        return new UserProfileResponse(
                member.getName(),
                member.getEmail(),
                member.getProfileImageUrl(),
                member.getProviderId());
    }
}
