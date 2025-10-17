package com.todoservice.dailygrowth.domain.task;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.enums.DayLabel;
import com.todoservice.dailygrowth.common.enums.Priority;
import com.todoservice.dailygrowth.common.enums.Status;
import com.todoservice.dailygrowth.common.exception.BaseException;
import com.todoservice.dailygrowth.domain.color.entity.Color;
import com.todoservice.dailygrowth.domain.project.application.ProjectService;
import com.todoservice.dailygrowth.domain.project.domain.entity.Project;
import com.todoservice.dailygrowth.domain.task.application.TaskFactory;
import com.todoservice.dailygrowth.domain.task.application.TaskService;
import com.todoservice.dailygrowth.domain.task.domain.entity.Task;
import com.todoservice.dailygrowth.domain.task.domain.port.TaskRepository;
import com.todoservice.dailygrowth.domain.task.domain.vo.Schedule;
import com.todoservice.dailygrowth.domain.task.presentation.dto.TaskCreateRequest;
import com.todoservice.dailygrowth.domain.task.presentation.dto.TaskUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock private TaskRepository taskRepository;
    @Mock private ProjectService projectService;
    @Mock private TaskFactory factory;
    @InjectMocks private TaskService taskService;

    @Test
    @DisplayName("listTask: 전체 조회")
    public void listTask() throws Exception {
        //when
        when(taskRepository.findAll()).thenReturn(List.of(mock(Task.class), mock(Task.class)));

        //then
        assertThat(taskService.listTask()).hasSize(2);
        verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("getTaskByIdOrThrow: 존재, 반환 / 미존재, BaseException 반환")
    public void getTaskByIdOrThrow() throws Exception {
        //given
        Task task = mock(Task.class);

        //when
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.findById(9L)).thenReturn(Optional.empty());

        //then
        assertThat(taskService.getTaskByIdOrThrow(1L)).isSameAs(task);
        assertThatThrownBy(() -> taskService.getTaskByIdOrThrow(9L))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.NOT_FOUND_TASK.getMessage());
    }

    @Test
    @DisplayName("createTask: Factory가 조립한 엔티티를 save한다.")
    public void createTask() throws Exception {
        //given
        TaskCreateRequest request = mock(TaskCreateRequest.class);
        Task task = mock(Task.class);

        //when
        when(factory.createTask(request)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        Task saved = taskService.createTask(request);

        //then
        assertThat(saved).isSameAs(task);
        verify(factory).createTask(request);
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("deleteTask: 삭제")
    public void deleteTask() throws Exception {
        //then
        taskService.deleteTask(3L);
        verify(taskRepository).deleteById(3L);
    }

    @Test
    @DisplayName("updateTask: projectId/colorId/schedule null 처리 및 변경 반영")
    public void updateTaskPartialUpdate() throws Exception {
        //given
        Task task = mock(Task.class);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        //when
        TaskUpdateRequest request = mock(TaskUpdateRequest.class);
        when(request.projectId()).thenReturn(null);
        when(request.schedule()).thenReturn(null);
        when(request.priority()).thenReturn(Priority.HIGH);
        when(request.title()).thenReturn("알고리즘 공부하기");
        when(request.description()).thenReturn("백준 1234");
        when(request.dayLabel()).thenReturn(DayLabel.AFTERNOON);
        when(request.status()).thenReturn(Status.PLANNING);

        Task updateTask = taskService.updateTask(request, 1L);

        //then
        verify(task).changeProject(any());
        verify(task).changeSchedule(any());
        verify(task).changePriority(Priority.HIGH);
        verify(task).changeTitle("알고리즘 공부하기");
        verify(task).changeDescription("백준 1234");
        verify(task).changeDayLabel(DayLabel.AFTERNOON);
        verify(task).changeStatus(Status.PLANNING);

        assertThat(updateTask).isSameAs(task);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateTask: projectId/colorId/schedule 제공 시 교체")
    public void updateTaskReplaceRefs() throws Exception {
        //given
        Task task = mock(Task.class);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Project project = mock(Project.class);
        Color color = mock(Color.class);
        when(projectService.getProjectByIdOrThrow(100L)).thenReturn(project);

        Schedule schedule = Schedule.of(
                LocalDate.of(2025, 1, 1), LocalTime.of(10, 0), true,
                LocalDate.of(2025, 12, 31), LocalTime.of(10, 0), true
        );

        //when
        TaskUpdateRequest request = mock(TaskUpdateRequest.class);
        when(request.projectId()).thenReturn(100L);
        when(request.schedule()).thenReturn(schedule);
        when(request.priority()).thenReturn(Priority.HIGH);
        when(request.priority()).thenReturn(Priority.HIGH);
        when(request.title()).thenReturn("알고리즘 공부하기");
        when(request.description()).thenReturn("백준 1234");
        when(request.dayLabel()).thenReturn(DayLabel.AFTERNOON);
        when(request.status()).thenReturn(Status.PLANNING);

        Task updateTask = taskService.updateTask(request, 1L);

        //then
        verify(task).changeProject(project);
        verify(task).changeSchedule(schedule);
        verify(task).changePriority(Priority.HIGH);
        verify(task).changeTitle("알고리즘 공부하기");
        verify(task).changeDescription("백준 1234");
        verify(task).changeDayLabel(DayLabel.AFTERNOON);
        verify(task).changeStatus(Status.PLANNING);

        assertThat(updateTask).isSameAs(task);
    }

    @Test
    @DisplayName("updateTaskStatus: 상태만 변경")
    public void updateTaskStatus() throws Exception {
        //given
        Task task = mock(Task.class);

        //when
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task update = taskService.updateTaskStatus(1L, Status.COMPLETED);

        //then
        assertThat(task).isSameAs(update);
        verify(task).changeStatus(Status.COMPLETED);
    }
}
