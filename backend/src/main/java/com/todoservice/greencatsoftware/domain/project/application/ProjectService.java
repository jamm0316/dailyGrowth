package com.todoservice.greencatsoftware.domain.project.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.todoservice.greencatsoftware.common.baseResponse.BaseResponseStatus;
import com.todoservice.greencatsoftware.common.enums.Status;
import com.todoservice.greencatsoftware.common.enums.Visibility;
import com.todoservice.greencatsoftware.common.exception.BaseException;
import com.todoservice.greencatsoftware.domain.color.application.ColorService;
import com.todoservice.greencatsoftware.domain.color.entity.Color;
import com.todoservice.greencatsoftware.domain.project.domain.entity.Project;
import com.todoservice.greencatsoftware.domain.project.domain.port.ProjectRepository;
import com.todoservice.greencatsoftware.domain.project.domain.vo.Period;
import com.todoservice.greencatsoftware.domain.project.presentation.dto.ProjectCreateRequest;
import com.todoservice.greencatsoftware.domain.project.presentation.dto.ProjectDetailResponse;
import com.todoservice.greencatsoftware.domain.project.presentation.dto.ProjectFieldUpdateRequest;
import com.todoservice.greencatsoftware.domain.project.presentation.dto.ProjectSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectFactory projectFactory;
    private final ProjectRepository projectRepository;
    private final ColorService colorService;

    public List<Project> listProject() {
        return projectRepository.findAll();
    }

    public List<ProjectSummaryResponse> summaryListProject() {return projectRepository.findProjectSummary();}

    public Project getProjectByIdOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_PROJECT));
    }

    public ProjectDetailResponse getProjectDetailWithProgress(Long id) {
        return projectRepository.findDetailWithProgress(id);
    }

    @Transactional(readOnly = true)
    public List<Project> searchProjects(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }
        return projectRepository.searchByName(keyword.trim());
    }

    @Transactional
    public Project createProject(User user, ProjectCreateRequest newProjectDTO) {
        log.info("[PROJECT_CREATE]: service.enter");
        Project project = projectFactory.createProject(user, newProjectDTO);
        log.info("[PROJECT_CREATE]: beforeProjectSave:{}", project);
        Project save = projectRepository.save(project);
        log.info("[PROJECT_CREATE]: afterProjectSave:{}", save);
        return save;
    }

    @Transactional
    public void deleteProjectById(Long id) {
        projectRepository.deleteById(id);
    }

    @Transactional
    public Project updateProjectField(Long id, ProjectFieldUpdateRequest request) {
        Project project = getProjectByIdOrThrow(id);

        updateProjectSwitch(request, project);
        return project;
    }

    private void updateProjectSwitch(ProjectFieldUpdateRequest request, Project project) {
        switch (request.fieldType()) {
            case "name" -> project.changeName((String) request.value());
            case "description" -> project.changeDescription((String) request.value());
            case "status" -> {
                if (request.value().equals("PLANNING")) {
                    project.changeStatus(Status.PLANNING);
                }
                if (request.value().equals("IN_PROGRESS")) {
                    project.changeStatus(Status.IN_PROGRESS);
                }
                if (request.value().equals("COMPLETED")) {
                    project.changeStatus(Status.COMPLETED);
                }
                if (request.value().equals("ON_HOLD")) {
                    project.changeStatus(Status.ON_HOLD);
                }
            }
            case "period" -> {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());

                Period requestPeriod = objectMapper.convertValue(request.value(), Period.class);

                LocalDate startDate = requestPeriod.startDate();
                LocalDate endDate = requestPeriod.endDate();
                LocalDate actualDate = requestPeriod.actualEndDate();

                project.changePeriod(Period.of(startDate, endDate, actualDate));
            }
            case "colorId" -> {
                Color color = colorService.getColorByIdOrThrow((Long) request.value());
                project.changeColor(color);
            }
            case "isPublic" -> project.changeIsPublic((Boolean) request.value());
            case "visibility" -> {
                if (request.value().equals("PRIVATE")) {
                    project.changeVisibility(Visibility.PRIVATE);
                }
                if (request.value().equals("PUBLIC")) {
                    project.changeVisibility(Visibility.PUBLIC);
                }
                if (request.value().equals("TEAM")) {
                    project.changeVisibility(Visibility.TEAM);
                }
            }
        }
    }
}
