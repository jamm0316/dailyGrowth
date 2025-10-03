package com.todoservice.greencatsoftware.domain.task.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.todoservice.greencatsoftware.common.baseResponse.BaseResponseStatus;
import com.todoservice.greencatsoftware.common.enums.DayLabel;
import com.todoservice.greencatsoftware.common.enums.Priority;
import com.todoservice.greencatsoftware.common.enums.Status;
import com.todoservice.greencatsoftware.common.exception.BaseException;
import com.todoservice.greencatsoftware.domain.project.application.ProjectService;
import com.todoservice.greencatsoftware.domain.project.domain.entity.Project;
import com.todoservice.greencatsoftware.domain.task.domain.entity.Task;
import com.todoservice.greencatsoftware.domain.task.domain.port.TaskRepository;
import com.todoservice.greencatsoftware.domain.task.domain.vo.Schedule;
import com.todoservice.greencatsoftware.domain.task.presentation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
    private final TaskFactory taskFactory;
    private final TaskRepository taskRepository;
    private final ProjectService projectService;

    public List<Task> listTask() {
        return taskRepository.findAll();
    }

    public List<TaskSummaryResponse> summaryListTask() {
        return taskRepository.summaryListTask();
    }

    public List<TaskSummaryResponse> todayListTask(LocalDate today) {
        return taskRepository.todayListTask(today);
    }

    public Task getTaskByIdOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_TASK));
    }

    public TaskDetailResponse getTaskDetailById(Long id) {
        return taskRepository.getTaskDetailById(id);
    }

    @Transactional
    public Task createTask(TaskCreateRequest newTaskDTO) {
        return taskRepository.save(taskFactory.createTask(newTaskDTO));
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public Task updateTask(TaskUpdateRequest request, Long id) {
        Task task = getTaskByIdOrThrow(id);
        Project project = (request.projectId() != null)
                ? projectService.getProjectByIdOrThrow(request.projectId())
                : task.getProject();
        Schedule schedule = (request.schedule() != null)
                ? request.schedule()
                : task.getSchedule();

        task.changeProject(project);
        task.changePriority(request.priority());
        task.changeTitle(request.title());
        task.changeDescription(request.description());
        task.changeDayLabel(request.dayLabel());
        task.changeSchedule(schedule);
        task.changeStatus(request.status());

        return task;
    }

    @Transactional
    public Task updateTaskField(Long id, TaskFieldUpdateRequest request) {
        Task task = getTaskByIdOrThrow(id);
        updateTaskSwitch(request, task);
        return task;
    }

    private static void updateTaskSwitch(TaskFieldUpdateRequest request, Task task) {
        switch (request.fieldType()) {
            case "title" -> task.changeTitle((String) request.value());
            case "description" -> task.changeDescription((String) request.value());
            case "status" -> {
                if (request.value().equals("PLANNING")) {
                    task.changeStatus(Status.PLANNING);
                }
                if (request.value().equals("IN_PROGRESS")) {
                    task.changeStatus(Status.IN_PROGRESS);
                }
                if (request.value().equals("COMPLETED")) {
                    task.changeStatus(Status.COMPLETED);
                }
                if (request.value().equals("ON_HOLD")) {
                    task.changeStatus(Status.ON_HOLD);
                }
            }
            case "schedule" -> {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());

                Schedule requestSchedule = objectMapper.convertValue(request.value(), Schedule.class);

                LocalDate startDate = requestSchedule.startDate();
                LocalTime startTime = requestSchedule.startTime();
                Boolean startTimeEnabled = requestSchedule.startTimeEnabled();
                LocalDate dueDate = requestSchedule.dueDate();
                LocalTime dueTime = requestSchedule.dueTime();
                Boolean dueTimeEnabled = requestSchedule.dueTimeEnabled();

                task.changeSchedule(Schedule.of(startDate, startTime, startTimeEnabled, dueDate, dueTime, dueTimeEnabled));
            }
            case "dayLabel" -> {
                if (request.value().equals("MORNING")) {
                    task.changeDayLabel(DayLabel.MORNING);
                }
                if (request.value().equals("AFTERNOON")) {
                    task.changeDayLabel(DayLabel.AFTERNOON);
                }
                if (request.value().equals("EVENING")) {
                    task.changeDayLabel(DayLabel.EVENING);
                }
            }
            case "priority" -> {
                if (request.value().equals("LOW")) {
                    task.changePriority(Priority.LOW);
                }
                if (request.value().equals("MEDIUM")) {
                    task.changePriority(Priority.MEDIUM);
                }
                if (request.value().equals("HIGH")) {
                    task.changePriority(Priority.HIGH);
                }
            }
        }
    }

    @Transactional
    public Task updateTaskStatus(Long id, Status status) {
        Task task = getTaskByIdOrThrow(id);
        task.changeStatus(status);
        return task;
    }

    //todo: 작업 대량 생성
    public void bulkCreateTasks(List<TaskCreateRequest> newTaskDTOList) {
        throw new UnsupportedOperationException("TODO: 작업 대량 생성 작성");
    }

    //todo: 작업 대량 수정
    public void bulkUpdateTasks(List<TaskCreateRequest> newTaskDTOList) {
        throw new UnsupportedOperationException("TODO: 작업 대량 수정 작성");
    }

    //todo: 작업 대량 삭제
    public void bulkDeleteTasks(List<Long> taskIdList) {
        throw new UnsupportedOperationException("TODO: 작업 대량 삭제 작성");
    }
}
