package com.todoservice.dailygrowth.domain.project.domain.port;

import com.todoservice.dailygrowth.domain.project.domain.entity.Project;
import com.todoservice.dailygrowth.domain.project.presentation.dto.ProjectDetailResponse;
import com.todoservice.dailygrowth.domain.project.presentation.dto.ProjectSummaryResponse;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    List<Project> findAll();
    List<ProjectSummaryResponse> findProjectSummary(Long userId);
    Project save(Project project);
    Optional<Project> findById(Long id);
    ProjectDetailResponse findDetailWithProgress(Long id);
    void deleteById(Long id);
    List<Project> searchByName(String keyword);
}
