package com.todoservice.greencatsoftware.domain.project.presentation;

import com.todoservice.greencatsoftware.common.baseResponse.BaseResponse;
import com.todoservice.greencatsoftware.config.security.principal.CustomUser;
import com.todoservice.greencatsoftware.domain.project.presentation.dto.ProjectDetailResponse;
import com.todoservice.greencatsoftware.domain.project.presentation.dto.ProjectCreateRequest;
import com.todoservice.greencatsoftware.domain.project.domain.entity.Project;
import com.todoservice.greencatsoftware.domain.project.application.ProjectService;
import com.todoservice.greencatsoftware.domain.project.presentation.dto.ProjectFieldUpdateRequest;
import com.todoservice.greencatsoftware.domain.project.presentation.dto.ProjectSummaryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("")
    public BaseResponse<List<Project>> listProject() {
        return new BaseResponse<>(projectService.listProject());
    }

    @GetMapping("/search")
    public BaseResponse<List<Project>> searchProjects(@RequestParam("keyword") String keyword) {
        return new BaseResponse<>(projectService.searchProjects(keyword));
    }

    @GetMapping("/summary")
    public BaseResponse<List<ProjectSummaryResponse>> summaryListProject() {
        return new BaseResponse<>(projectService.summaryListProject());
    }

    @GetMapping("/{id}")
    public BaseResponse<ProjectDetailResponse> getProjectDetailWithProgress(@PathVariable Long id) {
        return new BaseResponse<>(projectService.getProjectDetailWithProgress(id));
    }

    @PostMapping("")
    public BaseResponse<Project> createProject(
            @AuthenticationPrincipal CustomUser user,
            @Valid @RequestBody ProjectCreateRequest newProjectDTO) {
        return new BaseResponse<>(projectService.createProject(user, newProjectDTO));
    }

    @PatchMapping("/{id}/field")
    public BaseResponse<Project> updateProject(@RequestBody ProjectFieldUpdateRequest request, @PathVariable Long id) {
        return new BaseResponse<>(projectService.updateProjectField(id, request));
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProjectById(id);
        return new BaseResponse<>();
    }
}
