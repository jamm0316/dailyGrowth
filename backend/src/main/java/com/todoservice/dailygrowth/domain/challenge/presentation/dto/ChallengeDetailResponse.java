package com.todoservice.dailygrowth.domain.challenge.presentation.dto;

import com.todoservice.dailygrowth.common.enums.Status;
import com.todoservice.dailygrowth.domain.project.domain.vo.ChallengeDetails;
import com.todoservice.dailygrowth.domain.project.domain.vo.Period;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChallengeDetailResponse (
        @NotNull(message = "colorId는 필수 입니다.")
        Long colorId,  //프로젝트 컬러

        @NotBlank(message = "프로젝트 이름은 필수입니다.")
        String name,  //프로젝트 이름

        @NotNull(message = "상태값은 필수 입니다.")
        Status status,  //프로젝트 상태

        @NotNull(message = "기간은 필수 입니다.")
        Period period,
        String description,  //설명

        @NotNull(message = "챌린지 디테일은 필수 입니다.")
        ChallengeDetails challengeDetails  //챌린지 디테일
) {
    public ChallengeDetailResponse {
        // description이 빈 문자열이면 null로 정규화
        if (description != null && description.isBlank()) description = null;
    }
}
