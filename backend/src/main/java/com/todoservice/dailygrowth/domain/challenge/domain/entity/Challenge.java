package com.todoservice.dailygrowth.domain.challenge.domain.entity;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.enums.Status;
import com.todoservice.dailygrowth.common.exception.BaseException;
import com.todoservice.dailygrowth.domain.color.entity.Color;
import com.todoservice.dailygrowth.domain.member.domain.entity.Member;
import com.todoservice.dailygrowth.domain.project.domain.entity.Project;
import com.todoservice.dailygrowth.domain.challenge.domain.vo.ChallengeDetails;
import com.todoservice.dailygrowth.domain.project.domain.vo.Period;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("CHALLENGE")
public class Challenge extends Project {
    @Embedded
    private ChallengeDetails challengeDetails;

    @OneToMany(
            mappedBy = "challenge",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ChallengeParticipant> participants = new ArrayList<>();

    public Challenge(Color color,
                     Member member,
                     String name,
                     Status status,
                     Period period,
                     String description,
                     ChallengeDetails challengeDetails) {
        super(color, member, name, status, period, description);
        validateDomainInvariants(super.getPeriod(), challengeDetails);
        this.challengeDetails = challengeDetails;
    }

    private void validateDomainInvariants(Period period, ChallengeDetails challengeDetails) {
        if (period == null || period.isNull()) {
            throw new BaseException(BaseResponseStatus.CHALLENGE_PERIOD_UNDEFINED);
        }

        if (challengeDetails == null || challengeDetails.isNull()) {
            throw new BaseException(BaseResponseStatus.CHALLENGE_DETAILS_NOT_ALLOWED_FOR_NON_CHALLENGE);
        }
    }

    /**
     * ===== 챌린지 고유 메서드 ====
     **/
    static public Challenge create(Color color, Member member, String name, Status status, Period period,
                            String description, ChallengeDetails challengeDetails) {
        return new Challenge(color, member, name, status, period, description, challengeDetails);
    }

    public void addParticipant(Member member) {
        ChallengeParticipant challengeParticipant = ChallengeParticipant.create(this, member);
        participants.add(challengeParticipant);
        challengeDetails = challengeDetails.increaseParticipant();
    }

    public void removeParticipant(ChallengeParticipant participant) {
        this.participants.remove(participant);
        challengeDetails = challengeDetails.decreaseParticipant();
    }
}
