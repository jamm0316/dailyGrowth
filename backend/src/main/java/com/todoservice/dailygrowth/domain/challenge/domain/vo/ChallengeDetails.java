package com.todoservice.dailygrowth.domain.challenge.domain.vo;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.enums.ChallengeCategory;
import com.todoservice.dailygrowth.common.exception.BaseException;
import jakarta.persistence.Embeddable;

@Embeddable
public record ChallengeDetails(
        Integer participantCount,
        Integer targetParticipants,
        ChallengeCategory challengeCategory
) {
    public ChallengeDetails {
        validationChallengeDetails(participantCount, targetParticipants, challengeCategory);
    }

    private void validationChallengeDetails(int participantCount, int targetParticipants, ChallengeCategory challengeCategory) {
        if (targetParticipants <= 0) {
            throw new BaseException(BaseResponseStatus.INVALID_CHALLENGE_CAPACITY);
        }

        if (participantCount <= 0) {
            throw new BaseException(BaseResponseStatus.INVALID_CHALLENGE_PARTICIPANT_COUNT);
        }

        if (targetParticipants < participantCount) {
            throw new BaseException(BaseResponseStatus.CANNOT_JOIN_FULL_CHALLENGE);
        }
    }

    static public ChallengeDetails of(int targetParticipants, ChallengeCategory challengeCategory) {
        return new ChallengeDetails(1, targetParticipants, challengeCategory);
    }

    public ChallengeDetails increaseParticipant() {
        if (participantCount + 1 > targetParticipants) {
            throw new BaseException(BaseResponseStatus.CANNOT_JOIN_FULL_CHALLENGE);
        }
        return new ChallengeDetails(participantCount + 1, targetParticipants, challengeCategory);
    }

    public ChallengeDetails decreaseParticipant() {
        if (participantCount - 1 < 0) {
            throw new BaseException(BaseResponseStatus.INVALID_CHALLENGE_PARTICIPANT_COUNT);
        }
        return new ChallengeDetails(participantCount - 1, targetParticipants, challengeCategory);
    }

    public boolean isFull() {
        return participantCount == targetParticipants;
    }

    public boolean isNull() {
        return participantCount == null && targetParticipants == null;
    }
}
