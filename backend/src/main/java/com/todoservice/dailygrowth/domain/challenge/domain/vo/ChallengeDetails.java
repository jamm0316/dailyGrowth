package com.todoservice.dailygrowth.domain.challenge.domain.vo;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.enums.ChallengeCategory;
import com.todoservice.dailygrowth.common.exception.BaseException;
import jakarta.persistence.Embeddable;

@Embeddable
public record ChallengeDetails(
        Integer participantCount,
        Integer maxParticipant,
        ChallengeCategory challengeCategory
) {
    public ChallengeDetails {
        validationChallengeDetails(participantCount, maxParticipant, challengeCategory);
    }

    private void validationChallengeDetails(int participantCount, int targetParticipants, ChallengeCategory challengeCategory) {
        if (targetParticipants <= 0) {
            throw new BaseException(BaseResponseStatus.INVALID_CHALLENGE_CAPACITY);
        }

        if (targetParticipants < participantCount) {
            throw new BaseException(BaseResponseStatus.CHALLENGE_FULL);
        }
    }

    static public ChallengeDetails of(int targetParticipants, ChallengeCategory challengeCategory) {
        return new ChallengeDetails(0, targetParticipants, challengeCategory);
    }

    public ChallengeDetails increaseParticipant() {
        if (participantCount + 1 > maxParticipant) {
            throw new BaseException(BaseResponseStatus.CHALLENGE_FULL);
        }
        return new ChallengeDetails(participantCount + 1, maxParticipant, challengeCategory);
    }

    public ChallengeDetails decreaseParticipant() {
        if (participantCount - 1 < 0) {
            throw new BaseException(BaseResponseStatus.INVALID_CHALLENGE_PARTICIPANT_COUNT);
        }
        return new ChallengeDetails(participantCount - 1, maxParticipant, challengeCategory);
    }

    public boolean isFull() {
        return participantCount == maxParticipant;
    }

    public boolean isNull() {
        return participantCount == null && maxParticipant == null;
    }
}
