package com.todoservice.greencatsoftware.domain.task.domain.port;

import com.todoservice.greencatsoftware.domain.task.domain.entity.Task;
import com.todoservice.greencatsoftware.domain.task.presentation.dto.TaskDetailResponse;
import com.todoservice.greencatsoftware.domain.task.presentation.dto.TaskSummaryResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();
    List<TaskSummaryResponse> summaryListTask(Long userId);
    List<TaskSummaryResponse> todayListTask(LocalDate today);
    TaskDetailResponse getTaskDetailById(Long id);
    Task save(Task task);
    Optional<Task> findById(Long id);
    void deleteById(Long id);
}
