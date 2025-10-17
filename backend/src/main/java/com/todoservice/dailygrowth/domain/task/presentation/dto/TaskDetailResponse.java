package com.todoservice.dailygrowth.domain.task.presentation.dto;

import com.todoservice.dailygrowth.common.enums.DayLabel;
import com.todoservice.dailygrowth.common.enums.Priority;
import com.todoservice.dailygrowth.common.enums.Status;
import com.todoservice.dailygrowth.domain.task.domain.vo.Schedule;
import jakarta.validation.constraints.NotNull;

public record TaskDetailResponse(
        Long id,

        @NotNull(message = "colorId는 필수 입니다.")
        Long colorId,

        @NotNull(message = "projectId는 필수 입니다.")
        Long projectId,

        @NotNull(message = "priority는 필수 입니다.")
        Priority priority,

        @NotNull(message = "제목은 필수입니다.")
        String title,

        String description,
        DayLabel dayLabel,
        Schedule schedule,
        @NotNull(message = "상태는 필수 입니다.")
        Status status
) {
}
