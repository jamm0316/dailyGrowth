package com.todoservice.dailygrowth.domain.project.presentation.dto;

public record ProjectFieldUpdateRequest (
        String fieldType,
        Object value
) {
}
