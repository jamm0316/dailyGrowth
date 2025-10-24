package com.todoservice.dailygrowth.domain.challenge.infrastructure.persistence;

import com.todoservice.dailygrowth.domain.challenge.domain.entity.ChallengeParticipant;
import com.todoservice.dailygrowth.domain.challenge.domain.port.ChallengeParticipantRepository;
import com.todoservice.dailygrowth.domain.challenge.presentation.dto.ChallengeDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ChallengeParticipantJpaRepository extends JpaRepository<ChallengeParticipant, Long> {

    @Query("""
            SELECT new com.todoservice.dailygrowth.domain.challenge.presentation.dto.ChallengeDetailResponse
               (
                c.project.color.id,
                c.project.name,
                c.project.status,
                c.project.period,
                c.project.description,
                c.project.challengeDetails
               )
           FROM ChallengeParticipant c
           """)
    List<ChallengeDetailResponse> findAllChallenge();

    @Query("""
           SELECT new com.todoservice.dailygrowth.domain.challenge.presentation.dto.ChallengeDetailResponse
               (
                c.project.color.id,
                c.project.name,
                c.project.status,
                c.project.period,
                c.project.description,
                c.project.challengeDetails
               )
           FROM ChallengeParticipant c
           WHERE c.member.id = :userId
           """)
    List<ChallengeDetailResponse> findAllChallengeByMember(@Param("userId") Long userId);

    @Repository
    @RequiredArgsConstructor
    class ChallengeParticipantImpl implements ChallengeParticipantRepository {
        private final ChallengeParticipantJpaRepository jpa;

        @Override
        public List<ChallengeDetailResponse> findAllChallenge() {
            return List.of();
        }

        @Override
        public List<ChallengeDetailResponse> findAllChallengeByMember(Long userId) {
            return List.of();
        }

        @Override
        public void deleteById(Long id) { jpa.deleteById(id); }

        @Override
        public ChallengeParticipant save(ChallengeParticipant challengeParticipant) { return jpa.save(challengeParticipant); }
    }
}
