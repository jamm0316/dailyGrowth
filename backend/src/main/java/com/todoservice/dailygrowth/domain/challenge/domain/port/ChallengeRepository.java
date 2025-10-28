package com.todoservice.dailygrowth.domain.challenge.domain.port;

import com.todoservice.dailygrowth.domain.challenge.domain.entity.Challenge;
import com.todoservice.dailygrowth.domain.challenge.presentation.dto.ChallengeDetailResponse;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository {
    //todo: challengeSummaryResponse 로직 작성
    //List<ChallengeSummaryResponse> findProjectSummary(Long userId);
    Challenge save(Challenge challenge);
    Optional<Challenge> findById(Long id);
    void deleteById(Long id);
    List<Challenge> searchByName(String keyword);
}
