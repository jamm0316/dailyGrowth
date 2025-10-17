package com.todoservice.dailygrowth.domain.project.presentation;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponse;
import com.todoservice.dailygrowth.domain.project.presentation.dto.ProjectDetailResponse;
import com.todoservice.dailygrowth.domain.project.presentation.dto.ProjectCreateRequest;
import com.todoservice.dailygrowth.domain.project.domain.entity.Project;
import com.todoservice.dailygrowth.domain.project.application.ProjectService;
import com.todoservice.dailygrowth.domain.project.presentation.dto.ProjectFieldUpdateRequest;
import com.todoservice.dailygrowth.domain.project.presentation.dto.ProjectSummaryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
    public BaseResponse<List<ProjectSummaryResponse>> summaryListProject(@AuthenticationPrincipal User user) {
        return new BaseResponse<>(projectService.summaryListProject(Long.parseLong(user.getUsername())));
    }

    @GetMapping("/{id}")
    public BaseResponse<ProjectDetailResponse> getProjectDetailWithProgress(@PathVariable Long id) {
        return new BaseResponse<>(projectService.getProjectDetailWithProgress(id));
    }

    @PostMapping("")
    public BaseResponse<Project> createProject(
            @AuthenticationPrincipal User user,
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