package com.todoservice.dailygrowth.domain.task.presentation.dto;

public record TaskFieldUpdateRequest(
        String fieldType,
        Object value
) {
}
