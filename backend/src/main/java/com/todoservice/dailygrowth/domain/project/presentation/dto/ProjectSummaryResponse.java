package com.todoservice.dailygrowth.domain.project.presentation.dto;


import java.time.LocalDate;

public record ProjectSummaryResponse (
        Long id,
        Long colorId,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        double progress
) {

}
