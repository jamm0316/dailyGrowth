package com.todoservice.dailygrowth.domain.project.infrastructure.persistence;

import com.todoservice.dailygrowth.domain.project.domain.entity.Project;
import com.todoservice.dailygrowth.domain.project.domain.port.ProjectRepository;
import com.todoservice.dailygrowth.domain.project.presentation.dto.ProjectDetailResponse;
import com.todoservice.dailygrowth.domain.project.presentation.dto.ProjectSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface SpringDataProjectJpaRepository extends JpaRepository<Project, Long> {
    @Query("""
                SELECT new com.todoservice.dailygrowth.domain.project.presentation.dto.ProjectSummaryResponse
                    (
                    p.id,
                    p.color.id,
                    p.name,
                    p.period.startDate,
                    p.period.endDate,
                    CASE 
                        WHEN COUNT(t) = 0 THEN 0.0
                        ELSE SUM(
                             CASE 
                                 WHEN t.status = com.todoservice.dailygrowth.common.enums.Status.COMPLETED THEN 1
                                 ELSE 0
                             END          
                             ) * 1.0 / COUNT(t)
                    END
                    )
                FROM Project p
                    LEFT JOIN p.color c
                    LEFT JOIN Task t ON t.project.id = p.id
                WHERE p.member.id = :userId   
                GROUP BY p.id, p.color.id, p.name, p.period.startDate, p.period.endDate
                ORDER BY p.period.endDate ASC               
                """)
    List<ProjectSummaryResponse> findProjectSummary(@Param("userId") Long userId);

    @Query("""
           SELECT new com.todoservice.dailygrowth.domain.project.presentation.dto.ProjectDetailResponse
               (
                p.color.id,
                p.name,
                p.status,
                p.period,
                p.description,
                CASE 
                    WHEN COUNT(t) = 0 THEN 0.0
                    ELSE SUM(
                        CASE 
                            WHEN t.status = com.todoservice.dailygrowth.common.enums.Status.COMPLETED THEN 1
                            ELSE 0
                        END          
                        ) * 1.0 / COUNT(t)
                END
               )
           FROM Project p
               LEFT JOIN Task t ON t.project.id = p.id
           WHERE p.id = :projectId
           GROUP BY p.color.id, p.name, p.status, p.period, p.description
           """)
    ProjectDetailResponse findDetailWithProgress(@Param("projectId") Long projectId);

    @Query("""
           SELECT p
           FROM Project p
           WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           """)
    List<Project> searchByName(@Param("keyword") String keyword);

    @Repository
    @RequiredArgsConstructor
    class ProjectRepositoryImpl implements ProjectRepository {
        private final SpringDataProjectJpaRepository jpa;

        @Override
        public List<Project> findAll() {return jpa.findAll();}

        @Override
        public List<ProjectSummaryResponse> findProjectSummary(Long userId) {
            return jpa.findProjectSummary(userId);
        }

        @Override
        public Project save(Project project) {return jpa.save(project);}

        @Override
        public Optional<Project> findById(Long id) {return jpa.findById(id);}

        @Override
        public ProjectDetailResponse findDetailWithProgress(Long id) {return jpa.findDetailWithProgress(id);}

        @Override
        public void deleteById(Long id) {jpa.deleteById(id);}

        @Override
        public List<Project> searchByName(String keyword) {return jpa.searchByName(keyword);}

    }
}
