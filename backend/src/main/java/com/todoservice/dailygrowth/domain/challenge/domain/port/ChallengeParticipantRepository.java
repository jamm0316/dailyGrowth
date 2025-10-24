package com.todoservice.dailygrowth.domain.challenge.domain.port;

import com.todoservice.dailygrowth.domain.challenge.domain.entity.ChallengeParticipant;
import com.todoservice.dailygrowth.domain.challenge.presentation.dto.ChallengeDetailResponse;

import java.util.List;

public interface ChallengeParticipantRepository {
    List<ChallengeDetailResponse> findAllChallenge();
    List<ChallengeDetailResponse> findAllChallengeByMember(Long userId);
    void deleteById(Long id);
    ChallengeParticipant save(ChallengeParticipant challengeParticipant);
}
