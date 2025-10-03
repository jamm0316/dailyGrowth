package com.todoservice.greencatsoftware.domain.task.application;

import com.todoservice.greencatsoftware.domain.project.application.ProjectService;
import com.todoservice.greencatsoftware.domain.project.domain.entity.Project;
import com.todoservice.greencatsoftware.domain.task.domain.entity.Task;
import com.todoservice.greencatsoftware.domain.task.domain.vo.Schedule;
import com.todoservice.greencatsoftware.domain.task.presentation.dto.TaskCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskFactory {
    private final ProjectService projectService;

    public Task createTask(TaskCreateRequest request) {
        Project project = projectService.getProjectByIdOrThrow(request.projectId());
        Schedule schedule = Schedule.of(
                request.schedule().startDate(), request.schedule().startTime(), request.schedule().startTimeEnabled(),
                request.schedule().dueDate(), request.schedule().dueTime(), request.schedule().dueTimeEnabled());

        return (schedule.isNull())
                ? Task.create(project, request.priority(), request.title(), request.description(),
                request.dayLabel(), request.status())
                : Task.createWithSchedule(project, request.priority(), request.title(), request.description(),
                request.dayLabel(), schedule, request.status());
    }
}
