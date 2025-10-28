package com.todoservice.dailygrowth.domain.challenge.presentation;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponse;
import com.todoservice.dailygrowth.domain.challenge.application.ChallengeService;
import com.todoservice.dailygrowth.domain.challenge.presentation.dto.ChallengeCreateRequest;
import com.todoservice.dailygrowth.domain.project.application.ProjectService;
import com.todoservice.dailygrowth.domain.project.domain.entity.Project;
import com.todoservice.dailygrowth.domain.project.presentation.dto.ProjectCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/challenge")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;


    @PostMapping("")
    public BaseResponse<Project> createChallenge(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ChallengeCreateRequest newChallengeDTO) {
        return new BaseResponse<>(challengeService.createChallenge(user, newChallengeDTO));
    }
}
