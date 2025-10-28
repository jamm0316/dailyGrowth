package com.todoservice.dailygrowth.domain.challenge.infrastructure.persistence;

import com.todoservice.dailygrowth.domain.challenge.domain.entity.Challenge;
import com.todoservice.dailygrowth.domain.challenge.domain.port.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface SpringDataChallengeJpaRepository extends JpaRepository<Challenge, Long> {

    @Query("""
           SELECT c
           FROM Challenge c
           WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           """)
    List<Challenge> searchByName(@Param("keyword") String keyword);

    @Repository
    @RequiredArgsConstructor
    class ChallengeImpl implements ChallengeRepository {
        private final SpringDataChallengeJpaRepository jpa;

        @Override
        public Challenge save(Challenge challenge) { return jpa.save(challenge); }

        @Override
        public Optional<Challenge> findById(Long id) { return jpa.findById(id); }

        @Override
        public void deleteById(Long id) { jpa.deleteById(id); }

        @Override
        public List<Challenge> searchByName(String keyword) { return jpa.searchByName(keyword); }
    }
}
