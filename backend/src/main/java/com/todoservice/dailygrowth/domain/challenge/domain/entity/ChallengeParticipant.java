package com.todoservice.dailygrowth.domain.challenge.domain.entity;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.exception.BaseException;
import com.todoservice.dailygrowth.common.superEntity.SuperEntity;
import com.todoservice.dailygrowth.domain.member.domain.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeParticipant extends SuperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "챌린지 id는 필수 입니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_CHALLENGE_PARTICIPANT_CHALLENGE"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Challenge challenge;

    @NotNull(message = "멤버 id는 필수 입니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_CHALLENGE_PARTICIPANT_MEMBER"))
    private Member member;

    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedDateTime;

    public ChallengeParticipant(Challenge challenge, Member member) {
        validateDomainInvariants(challenge, member);
        this.challenge = challenge;
        this.member = member;
        this.joinedDateTime = LocalDateTime.now();
    }

    private void validateDomainInvariants(Challenge challenge, Member member) {
        //1. 기본 null 확인
        if (challenge == null) {
            throw new BaseException(BaseResponseStatus.MISSING_PROJECT_FOR_CHALLENGE);
        }
        if (member == null) {
            throw new BaseException(BaseResponseStatus.MISSING_MEMBER_FOR_CHALLENGE);
        }

        if (challenge.getPeriod() == null || challenge.getPeriod().isNull()) {
            throw new BaseException(BaseResponseStatus.CHALLENGE_PERIOD_UNDEFINED);
        }

        //2. 날짜 유효성 검사
        LocalDate joinedDate = LocalDate.now();
        LocalDate start = challenge.getPeriod().startDate();
        LocalDate end = challenge.getPeriod().endDate();

        //2-1. joinedDateTime이 시작 날짜보다 이전인지 확인
        if (joinedDate.isBefore(start)) {
            throw new BaseException(BaseResponseStatus.CHALLENGE_NOT_START);
        }

        //2-2. joinedDateTime이 종료 날짜보다 이후인지 확인
        if (end != null && joinedDate.isAfter(end)) {
            throw new BaseException(BaseResponseStatus.CHALLENGE_ENDED);
        }
    }

    static public ChallengeParticipant create(Challenge challenge, Member member) {
        return new ChallengeParticipant(challenge, member);
    }
}
