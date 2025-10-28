package com.todoservice.dailygrowth.domain.challenge.application;

import com.todoservice.dailygrowth.domain.challenge.domain.entity.Challenge;
import com.todoservice.dailygrowth.domain.challenge.presentation.dto.ChallengeCreateRequest;
import com.todoservice.dailygrowth.domain.color.application.ColorService;
import com.todoservice.dailygrowth.domain.member.application.MemberService;
import com.todoservice.dailygrowth.domain.member.domain.entity.Member;
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
    private final MemberService memberService;

    @Transactional
    public Project createChallenge(User user, ChallengeCreateRequest newChallengeDTO) {
        Challenge challenge = challengeFactory.createChallenge(user, newChallengeDTO);

        long memberId = Long.parseLong(user.getUsername());
        Member member = memberService.getMemberByIdOrThrow(memberId);
        challenge.addParticipant(member);

        return projectRepository.save(challenge);
    }
}
