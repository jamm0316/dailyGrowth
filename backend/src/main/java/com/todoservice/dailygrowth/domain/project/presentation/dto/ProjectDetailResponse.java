package com.todoservice.dailygrowth.domain.project.presentation.dto;

import com.todoservice.dailygrowth.common.enums.Status;
import com.todoservice.dailygrowth.common.enums.Visibility;
import com.todoservice.dailygrowth.domain.project.domain.vo.Period;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjectDetailResponse(
        @NotNull(message = "colorId는 필수 입니다.")
        Long colorId,  //프로젝트 컬러

        @NotBlank(message = "프로젝트 이름은 필수입니다.")
        String name,  //프로젝트 이름

        @NotNull(message = "상태값은 필수 입니다.")
        Status status,  //프로젝트 상태
        Period period,
        String description,  //설명

        @NotNull(message = "공개 여부는 필수입니다.")
        Boolean isPublic,  //공개 여부

        @NotNull(message = "공개 범위는 필수입니다.")
        Visibility visibility,  //공개 범위

        double progress
) {
}
