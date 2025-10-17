package com.todoservice.dailygrowth.domain.project.presentation.dto;

import com.todoservice.dailygrowth.common.enums.Visibility;

import java.time.LocalDate;

public record ProjectSummaryResponse (
        Long id,
        Long colorId,
        String name,
        Visibility visibility,
        LocalDate startDate,
        LocalDate endDate,
        double progress
) {

}
