package com.todoservice.dailygrowth.domain.challenge.application;

import com.todoservice.dailygrowth.domain.challenge.presentation.dto.ChallengeCreateRequest;
import com.todoservice.dailygrowth.domain.color.application.ColorService;
import com.todoservice.dailygrowth.domain.project.domain.entity.Project;
import com.todoservice.dailygrowth.domain.project.domain.port.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {
    private final ChallengeFactory challengeFactory;
    private final ProjectRepository projectRepository;
    private final ColorService colorService;

    @Transactional
    public Project createChallenge(User user, ChallengeCreateRequest newChallengeDTO) {
        return projectRepository.save(challengeFactory.createChallenge(user, newChallengeDTO));
    }
}
