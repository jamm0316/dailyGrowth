package com.todoservice.dailygrowth.domain.challengeParticipant;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.enums.ChallengeCategory;
import com.todoservice.dailygrowth.common.enums.Status;
import com.todoservice.dailygrowth.common.exception.BaseException;
import com.todoservice.dailygrowth.domain.auth.domain.oauth.vo.OAuth2Provider;
import com.todoservice.dailygrowth.domain.challenge.domain.entity.Challenge;
import com.todoservice.dailygrowth.domain.challenge.domain.entity.ChallengeParticipant;
import com.todoservice.dailygrowth.domain.color.entity.Color;
import com.todoservice.dailygrowth.domain.member.domain.entity.Member;
import com.todoservice.dailygrowth.domain.challenge.domain.vo.ChallengeDetails;
import com.todoservice.dailygrowth.domain.project.domain.vo.Period;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ChallengeParticipantTest {
    private Member member;
    private Challenge challenge;
    private ChallengeParticipant challengeParticipant;

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
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);
        Color color = Color.create("RED", "FF0000");
        Period period = Period.of(startDate, endDate, null);
        ChallengeDetails challengeDetails = ChallengeDetails.of(3, ChallengeCategory.WORKOUT);

        //when
        challenge = Challenge.create(
                color,
                member,
                "   나의 프로젝트    ",
                Status.PLANNING,
                period,
                "   잘해보자구~",
                challengeDetails);

        //when
        challengeParticipant =
                ChallengeParticipant.create(challenge, member);
    }

    @Test
    @DisplayName("정상 생성")
    public void create_ok () throws Exception {
        //then
        assertThat(challengeParticipant.getChallenge().getName()).isEqualTo("나의 프로젝트");
        assertThat(challengeParticipant.getChallenge().getStatus()).isEqualTo(Status.PLANNING);
        assertThat(challengeParticipant.getChallenge().getPeriod().startDate()).isEqualTo(LocalDate.now().minusDays(1));
        assertThat(challengeParticipant.getChallenge().getPeriod().endDate()).isEqualTo(LocalDate.now().plusDays(1));
        assertThat(challengeParticipant.getChallenge().getPeriod().actualEndDate()).isNull();
        assertThat(challengeParticipant.getChallenge().getDescription()).isEqualTo("잘해보자구~");
        assertThat(challengeParticipant.getChallenge().getColor().getHexCode()).isEqualTo("FF0000");
        assertThat(challengeParticipant.getChallenge().getColor().getName()).isEqualTo("RED");
        assertThat(challengeParticipant.getChallenge().getChallengeDetails().challengeCategory()).isEqualTo(ChallengeCategory.WORKOUT);
        assertThat(challengeParticipant.getChallenge().getChallengeDetails().participantCount()).isEqualTo(1);
        assertThat(challengeParticipant.getChallenge().getChallengeDetails().targetParticipants()).isEqualTo(3);

    }

    @Test
    @DisplayName("검증 실패: project와 member가 null 이면 BaseException 반환")
    public void create_fail_validation() throws Exception {
        //then
        //project null
        assertThatThrownBy(() -> ChallengeParticipant.create(null, member))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.MISSING_PROJECT_FOR_CHALLENGE.getMessage());

        //member null
        assertThatThrownBy(() -> ChallengeParticipant.create(challenge, null))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.MISSING_MEMBER_FOR_CHALLENGE.getMessage());
    }

    @Test
    @DisplayName("검증 실패: project period가 null이면 BaseException 반환")
    public void create_fail_validation_project_period_is_null() throws Exception {
        //given
        member = Member.create(
                "member1@test.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr123",
                "12345",
                "null",
                "testName");
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
    @DisplayName("검증 실패: project period가 시작 전이면 BaseException 반환")
    public void create_fail_validation_project_period_before_start () throws Exception {
        //given
        LocalDate changeStart = LocalDate.now().plusDays(1);
        Period periodChangeStart = Period.of(changeStart, challenge.getPeriod().endDate(), challenge.getPeriod().actualEndDate());

        challenge.changePeriod(periodChangeStart);

        //when
        assertThatThrownBy(() -> ChallengeParticipant.create(challenge, member))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.CHALLENGE_NOT_START.getMessage());
    }

    @Test
    @DisplayName("검증 실패: project period가 마감 후면 BaseException 반환")
    public void create_fail_validation_project_period_after_end () throws Exception {
        //given
        LocalDate changeEnd = LocalDate.now().minusDays(1);
        Period periodChangeEnd = Period.of(challenge.getPeriod().startDate(), changeEnd, challenge.getPeriod().actualEndDate());

        challenge.changePeriod(periodChangeEnd);

        //when
        assertThatThrownBy(() -> ChallengeParticipant.create(challenge, member))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.CHALLENGE_ENDED.getMessage());
    }

    @Test
    @DisplayName("검증 실패: project status가 COMPLETE이면 BaseException 반환")
    public void create_fail_validation_project_status_complete() throws Exception {
        //given
        challenge.changeStatus(Status.COMPLETED);

        //when
        assertThatThrownBy(() -> ChallengeParticipant.create(challenge, member))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.CHALLENGE_COMPLETED.getMessage());
    }

    @Test
    @DisplayName("수정 실패: ChallengeDetails의 participantCount가 targetParticipant보다 크면 BaseException 반환")
    public void create_fail_validation_challenge_details() throws Exception {
        //given
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);
        Color color = Color.create("RED", "FF0000");
        Period period = Period.of(startDate, endDate, null);
        ChallengeDetails challengeDetails = ChallengeDetails.of(1, ChallengeCategory.WORKOUT);

        //when
        Challenge newChallenge = Challenge.create(
                color,
                member,
                "   나의 프로젝트    ",
                Status.PLANNING,
                period,
                "   잘해보자구~",
                challengeDetails);

        //then
        assertThatThrownBy(() -> newChallenge.getChallengeDetails().increaseParticipant())
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.CANNOT_JOIN_FULL_CHALLENGE.getMessage());
    }
    @Test
    @DisplayName("수정 실패: ChallengeDetails의 participantCount가 0보다 작으면 BaseException 반환")
    public void create_fail_validation_challenge_details_decrease() throws Exception {
        //given
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);
        Color color = Color.create("RED", "FF0000");
        Period period = Period.of(startDate, endDate, null);
        ChallengeDetails challengeDetails = ChallengeDetails.of(1, ChallengeCategory.WORKOUT);

        //when
        Challenge newChallenge = Challenge.create(
                color,
                member,
                "   나의 프로젝트    ",
                Status.PLANNING,
                period,
                "   잘해보자구~",
                challengeDetails);

        //then
        assertThatThrownBy(() -> newChallenge.getChallengeDetails().decreaseParticipant())
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.INVALID_CHALLENGE_PARTICIPANT_COUNT.getMessage());
    }
}
