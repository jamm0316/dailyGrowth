package com.todoservice.dailygrowth.domain.project.presentation.dto;

import com.todoservice.dailygrowth.common.enums.Status;
import com.todoservice.dailygrowth.common.enums.Visibility;
import com.todoservice.dailygrowth.domain.project.domain.entity.ProjectType;
import com.todoservice.dailygrowth.domain.project.domain.vo.Period;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProjectCreateRequest(
        @NotNull(message = "colorId는 필수 입니다.")
        Long colorId,  //프로젝트 컬러

        @NotBlank(message = "프로젝트 이름은 필수입니다.")
        String name,  //프로젝트 이름

        @NotNull(message = "상태값은 필수 입니다.")
        Status status,  //프로젝트 상태

        @NotNull(message = "프로젝트 타입은 필수 입니다.")
        ProjectType projectType,

        Period period,
        String description,  //설명

        @NotNull(message = "공개 여부는 필수입니다.")
        Boolean isPublic,  //공개 여부

        @NotNull(message = "공개 범위는 필수입니다.")
        Visibility visibility  //공개 범위
) {
    public ProjectCreateRequest {
        // description이 빈 문자열이면 null로 정규화
        if (description != null && description.isBlank()) description = null;
    }
}
