package com.todoservice.dailygrowth.domain.challenge.domain.vo;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.exception.BaseException;
import jakarta.persistence.Embeddable;

@Embeddable
public record ChallengeDetails(
        Integer participantCount,
        Integer targetParticipants
) {
    public ChallengeDetails {
        validationChallengeDetails(participantCount, targetParticipants);
    }

    private void validationChallengeDetails(int participantCount, int targetParticipants) {
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

    static public ChallengeDetails of(int targetParticipants) {
        return new ChallengeDetails(1, targetParticipants);
    }

    public ChallengeDetails increaseParticipant() {
        if (participantCount + 1 > targetParticipants) {
            throw new BaseException(BaseResponseStatus.CANNOT_JOIN_FULL_CHALLENGE);
        }
        return new ChallengeDetails(participantCount + 1, targetParticipants);
    }

    public ChallengeDetails decreaseParticipant() {
        if (participantCount - 1 < 0) {
            throw new BaseException(BaseResponseStatus.INVALID_CHALLENGE_PARTICIPANT_COUNT);
        }
        return new ChallengeDetails(participantCount - 1, targetParticipants);
    }

    public boolean isFull() {
        return participantCount == targetParticipants;
    }

    public boolean isNull() {
        return participantCount == null && targetParticipants == null;
    }
}
