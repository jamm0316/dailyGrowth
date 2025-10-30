package com.todoservice.dailygrowth.domain.challenge;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.enums.ChallengeCategory;
import com.todoservice.dailygrowth.common.enums.Status;
import com.todoservice.dailygrowth.common.exception.BaseException;
import com.todoservice.dailygrowth.domain.auth.domain.oauth.vo.OAuth2Provider;
import com.todoservice.dailygrowth.domain.challenge.domain.entity.Challenge;
import com.todoservice.dailygrowth.domain.challenge.domain.vo.ChallengeDetails;
import com.todoservice.dailygrowth.domain.color.entity.Color;
import com.todoservice.dailygrowth.domain.member.domain.entity.Member;
import com.todoservice.dailygrowth.domain.project.domain.vo.Period;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ChallengeTest {
    private static final Logger log = LoggerFactory.getLogger(ChallengeTest.class);
    private Member member;
    private Challenge challenge;

    @BeforeEach
    public void createChallengeParticipant() throws Exception {
        //given
        member = Member.create(
                "member1@test.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr123",
                "12345",
                "null",
                "testName");
        ReflectionTestUtils.setField(member, "id", 1L);
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);
        Color color = Color.create("RED", "FF0000");
        Period period = Period.of(startDate, endDate, null);
        ChallengeDetails challengeDetails = ChallengeDetails.of(2, ChallengeCategory.WORKOUT);

        challenge = Challenge.create(
                color,
                member,
                "   나의 프로젝트    ",
                Status.PLANNING,
                period,
                "   잘해보자구~",
                challengeDetails);

    }
    //todo: challenge와 challengeDetails, 고유메서드 테스트 코드 작성(아래 모두 제거 후 수정해야함)
    @Test
    @DisplayName("검증 실패: project period가 null이면 BaseException 반환")
    public void create_fail_validation_project_period_is_null() throws Exception {
        //given
        Color color = Color.create("RED", "FF0000");
        ChallengeDetails challengeDetails = ChallengeDetails.of(3, ChallengeCategory.WORKOUT);

        //when
        assertThatThrownBy(() -> Challenge.create(
                color,
                member,
                "   나의 프로젝트    ",
                Status.PLANNING,
                null,
                "   잘해보자구~",
                challengeDetails))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.CHALLENGE_PERIOD_UNDEFINED.getMessage());
    }

    @Test
    @DisplayName("검증 실패: challenge addParticipant에서 status.COMPLETE이면 BaseException 반환")
    public void challenge_addParticipant_fail_validation_status() throws Exception {
        //given
        challenge.changeStatus(Status.COMPLETED);

        Member newMember = Member.create(
                "member2@test.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr456",
                "12345",
                "null",
                "testName2");

        //when
        assertThatThrownBy(() -> challenge.addParticipant(newMember))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.CHALLENGE_COMPLETED.getMessage());
    }

    @Test
    @DisplayName("검증 실패: challenge addParticipant에서 인원이 초과된 상태이면 BaseException 반환")
    public void challenge_addParticipant_fail_validation_participant_over() throws Exception {
        //given
        Member member1 = Member.create(
                "member2@test.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr456",
                "12345",
                "null",
                "testName2");

        Member member2 = Member.create(
                "member3@test.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr789",
                "12345",
                "null",
                "testName3");
        ReflectionTestUtils.setField(member1, "id", 2L);
        ReflectionTestUtils.setField(member2, "id", 3L);

        //when
        challenge.addParticipant(member1);

        //then
        assertThatThrownBy(() -> challenge.addParticipant(member2))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.CHALLENGE_FULL.getMessage());
    }

    @Test
    @DisplayName("검증 실패: challenge addParticipant에서 이미 참여한 상태이면 BaseException 반환")
    public void challenge_addParticipant_fail_validation_already() throws Exception {
        //then
        assertThatThrownBy(() -> challenge.addParticipant(member))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.ALREADY_PARTICIPATING_IN_CHALLENGE.getMessage());
    }

    @Test
    @DisplayName("성공: challenge addParticipant에 성공")
    public void challenge_addParticipant_success() throws Exception {
        //given
        Member member1 = Member.create(
                "member2@test.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr456",
                "12345",
                "null",
                "testName2");
        ReflectionTestUtils.setField(member1, "id", 2L);

        //when & then
        assertThat(challenge.getParticipants().size()).isEqualTo(1);
        challenge.addParticipant(member1);
        assertThat(challenge.getParticipants().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("검증 실패: challenge removeParticipant에서 status.COMPLETE라면 BaseException 반환")
    public void challenge_removeParticipant_fail_validation_status_complete() throws Exception {
        Member member1 = Member.create(
                "member2@test.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr456",
                "12345",
                "null",
                "testName2");
        ReflectionTestUtils.setField(member1, "id", 2L);

        //when
        challenge.addParticipant(member1);
        challenge.changeStatus(Status.COMPLETED);
        //then
        assertThatThrownBy(() -> challenge.removeParticipant(member1))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.CHALLENGE_COMPLETED.getMessage());
    }

    @Test
    @DisplayName("검증 실패: challenge removeParticipant에서 참여자 목록에 없는 사용자라면 BaseException 반환")
    public void challenge_removeParticipant_fail_validation_not_found() throws Exception {
        Member member1 = Member.create(
                "member2@test.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr456",
                "12345",
                "null",
                "testName2");
        ReflectionTestUtils.setField(member1, "id", 2L);

        //then
        assertThatThrownBy(() -> challenge.removeParticipant(member1))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.PARTICIPANT_NOT_FOUND_INT_CHALLENGE.getMessage());
    }

    @Test
    @DisplayName("검증 실패: challenge removeParticipant에서 소유자가 나가려고 하면 BaseException 반환")
    public void challenge_removeParticipant_fail_validation_is_owner() throws Exception {
        //then
        assertThatThrownBy(() -> challenge.removeParticipant(member))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.CHALLENGE_OWNER_CANNOT_LEAVE.getMessage());
    }

    @Test
    @DisplayName("성공: challenge removeParticipant에 성공")
    public void challenge_removeParticipant_success() throws Exception {
        Member member1 = Member.create(
                "member2@test.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr456",
                "12345",
                "null",
                "testName2");
        ReflectionTestUtils.setField(member1, "id", 2L);

        //when & then
        challenge.addParticipant(member1);
        assertThat(challenge.getParticipants().size()).isEqualTo(2);

        challenge.removeParticipant(member1);
        assertThat(challenge.getParticipants().size()).isEqualTo(1);
    }
}
