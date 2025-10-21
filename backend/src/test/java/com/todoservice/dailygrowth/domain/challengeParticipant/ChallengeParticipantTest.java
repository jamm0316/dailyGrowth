package com.todoservice.dailygrowth.domain.challengeParticipant;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.enums.Status;
import com.todoservice.dailygrowth.common.enums.Visibility;
import com.todoservice.dailygrowth.common.exception.BaseException;
import com.todoservice.dailygrowth.domain.auth.domain.oauth.vo.OAuth2Provider;
import com.todoservice.dailygrowth.domain.challenge.entity.ChallengeParticipant;
import com.todoservice.dailygrowth.domain.color.entity.Color;
import com.todoservice.dailygrowth.domain.member.domain.entity.Member;
import com.todoservice.dailygrowth.domain.project.domain.entity.Project;
import com.todoservice.dailygrowth.domain.project.domain.entity.ProjectType;
import com.todoservice.dailygrowth.domain.project.domain.vo.Period;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

public class ChallengeParticipantTest {
    private Member member;
    private Project project;
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

        //when
        project = Project.createWithPeriod(
                color,
                member,
                "   나의 프로젝트    ",
                Status.PLANNING,
                ProjectType.CHALLENGE,
                period,
                "   잘해보자구~",
                true,
                Visibility.PUBLIC);

        //when
        challengeParticipant =
                ChallengeParticipant.create(project, member);
    }

    @Test
    @DisplayName("정상 생성")
    public void create_ok () throws Exception {
        //then
        assertThat(challengeParticipant.getProject().getName()).isEqualTo("나의 프로젝트");
        assertThat(challengeParticipant.getProject().getStatus()).isEqualTo(Status.PLANNING);
        assertThat(challengeParticipant.getProject().getProjectType()).isEqualTo(ProjectType.CHALLENGE);
        assertThat(challengeParticipant.getProject().getPeriod().startDate()).isEqualTo(LocalDate.now().minusDays(1));
        assertThat(challengeParticipant.getProject().getPeriod().endDate()).isEqualTo(LocalDate.now().plusDays(1));
        assertThat(challengeParticipant.getProject().getPeriod().actualEndDate()).isNull();
        assertThat(challengeParticipant.getProject().getDescription()).isEqualTo("잘해보자구~");
        assertThat(challengeParticipant.getProject().getIsPublic()).isTrue();
        assertThat(challengeParticipant.getProject().getVisibility()).isEqualTo(Visibility.PUBLIC);
        assertThat(challengeParticipant.getProject().getColor().getHexCode()).isEqualTo("FF0000");
        assertThat(challengeParticipant.getProject().getColor().getName()).isEqualTo("RED");
    }

    @Test
    @DisplayName("검증 실패: project와 member가 null 이면 BaseException 반환")
    public void create_fail_validation() throws Exception {
        //then
        //project null
        assertThatThrownBy(() -> ChallengeParticipant.create(null, member))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.MISSING_PROJECT_FOR_CHALLENGE.getMessage());

        //member null
        assertThatThrownBy(() -> ChallengeParticipant.create(project, null))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.MISSING_MEMBER_FOR_CHALLENGE.getMessage());
    }

    @Test
    @DisplayName("검증 실패: projectType이 CHALLENGE가 아니면 BaseException 반환")
    public void create_fail_validation_project_type() throws Exception {
        //given
        project.changeProjectType(ProjectType.PERSONAL);

        //when
        assertThatThrownBy(() -> ChallengeParticipant.create(project, member))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.NOT_A_CHALLENGE_PROJECT.getMessage());
        //then

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

        //when
        project = Project.create(
                color,
                member,
                "   나의 프로젝트    ",
                Status.PLANNING,
                ProjectType.CHALLENGE,
                "   잘해보자구~",
                true,
                Visibility.PUBLIC);

        //when
        assertThatThrownBy(() -> ChallengeParticipant.create(project, member))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.CHALLENGE_PERIOD_UNDEFINED.getMessage());
    }

    @Test
    @DisplayName("검증 실패: project period가 시작 전이면 BaseException 반환")
    public void create_fail_validation_project_period_before_start () throws Exception {
        //given
        LocalDate changeStart = LocalDate.now().plusDays(1);
        Period periodChangeStart = Period.of(changeStart, project.getPeriod().endDate(), project.getPeriod().actualEndDate());

        project.changePeriod(periodChangeStart);

        //when
        assertThatThrownBy(() -> ChallengeParticipant.create(project, member))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.CHALLENGE_NOT_START.getMessage());
    }

    @Test
    @DisplayName("검증 실패: project period가 마감 후면 BaseException 반환")
    public void create_fail_validation_project_period_after_end () throws Exception {
        //given
        LocalDate changeEnd = LocalDate.now().minusDays(1);
        Period periodChangeEnd = Period.of(project.getPeriod().startDate(), changeEnd, project.getPeriod().actualEndDate());

        project.changePeriod(periodChangeEnd);

        //when
        assertThatThrownBy(() -> ChallengeParticipant.create(project, member))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.CHALLENGE_ENDED.getMessage());
    }

    @Test
    @DisplayName("검증 실패: project status가 COMPLETE이면 BaseException 반환")
    public void create_fail_validation_project_status_complete() throws Exception {
        //given
        project.changeStatus(Status.COMPLETED);

        //when
        assertThatThrownBy(() -> ChallengeParticipant.create(project, member))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.CHALLENGE_COMPLETED.getMessage());
    }

    @Test
    @DisplayName("검증 실패: project visibility가 PRIVATE이면 BaseException 반환")
    public void create_fail_validation_project_visibility_private() throws Exception {
        //given
        project.changeVisibility(Visibility.PRIVATE);

        //when
        assertThatThrownBy(() -> ChallengeParticipant.create(project, member))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.CHALLENGE_CANNOT_BE_PRIVATE.getMessage());
    }

    @Test
    @DisplayName("검증 실패: project isPublic이 false이면 BaseException 반환")
    public void create_fail_validation_project_isPublic_false() throws Exception {
        //given
        project.changeIsPublic(false);

        //when
        assertThatThrownBy(() -> ChallengeParticipant.create(project, member))
                .isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.CHALLENGE_CANNOT_BE_NO_PUBLIC.getMessage());
    }
}
