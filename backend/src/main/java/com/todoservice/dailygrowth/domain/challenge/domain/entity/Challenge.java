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
        Challenge challenge = new Challenge(color, member, name, status, period, description, challengeDetails);
        challenge.addParticipant(member);
        return challenge;
    }

    public void addParticipant(Member member) {
        //1. 이미 완료된 프로젝트인지 확인
        if (this.getStatus() == Status.COMPLETED) {
            throw new BaseException(BaseResponseStatus.CHALLENGE_COMPLETED);
        }

        //2. 이미 참여한 챌린지인지 검증
        if (this.participants.stream().anyMatch(p -> p.getMember().getId().equals(member.getId()))) {
            throw new BaseException(BaseResponseStatus.ALREADY_PARTICIPATING_IN_CHALLENGE);
        }

        //3. 최대 수용인원보다 현재 참여자 사이즈가 크거나 같은지 확인
        if (challengeDetails.isFull()) {
            throw new BaseException(BaseResponseStatus.CHALLENGE_FULL);
        }

        ChallengeParticipant challengeParticipant = ChallengeParticipant.create(this, member);
        participants.add(challengeParticipant);
        challengeDetails = challengeDetails.increaseParticipant();
    }

    public void removeParticipant(Member member) {
        //1. 이미 완료된 프로젝트인지 확인
        if (this.getStatus() == Status.COMPLETED) {
            throw new BaseException(BaseResponseStatus.CHALLENGE_COMPLETED);
        }

        //2. 참여자 목록에 있는 사람인지 확인
        ChallengeParticipant challengeParticipant = this.participants.stream()
                .filter(p -> p.getMember().getId().equals(member.getId()))
                .findFirst()
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PARTICIPANT_NOT_FOUND_INT_CHALLENGE));

        //3. 챌린지 소유자는 나갈 수 없다. (챌린지 소유자가 나가면 Challenge 인스턴스를 삭제)
        if (this.getMember().getId().equals(member.getId())) {
            throw new BaseException(BaseResponseStatus.CHALLENGE_OWNER_CANNOT_LEAVE);
        }

        this.participants.remove(challengeParticipant);
        challengeDetails = challengeDetails.decreaseParticipant();
    }
}
