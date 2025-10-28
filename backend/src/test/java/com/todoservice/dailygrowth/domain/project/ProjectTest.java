package com.todoservice.dailygrowth.domain.project;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.enums.Status;
import com.todoservice.dailygrowth.common.exception.BaseException;
import com.todoservice.dailygrowth.domain.auth.domain.oauth.vo.OAuth2Provider;
import com.todoservice.dailygrowth.domain.color.entity.Color;
import com.todoservice.dailygrowth.domain.member.domain.entity.Member;
import com.todoservice.dailygrowth.domain.project.domain.entity.PersonalProject;
import com.todoservice.dailygrowth.domain.project.domain.vo.Period;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProjectTest {

    private Member member = Member.create(
            "member1@test.com",
            OAuth2Provider.KAKAO,
            "asevanoeqointqewr123",
            "12345",
            "null",
            "testName");

    @Test
    @DisplayName("정상 생성: 기간 없이 생성")
    public void create_without_period_ok() throws Exception {
        //given
        Color color = Color.create("RED", "FF0000");

        //when
        PersonalProject project = PersonalProject.create(
                color,
                member,
                "   나의 프로젝트    ",
                Status.PLANNING,
                "   잘해보자구~");

        //then
        assertThat(project.getName()).isEqualTo("나의 프로젝트");
        assertThat(project.getStatus()).isEqualTo(Status.PLANNING);
        assertThat(project.getDescription()).isEqualTo("잘해보자구~");
        assertThat(project.getColor().getHexCode()).isEqualTo("FF0000");
        assertThat(project.getColor().getName()).isEqualTo("RED");
    }
    
    @Test
    @DisplayName("저장 생성: 기간 포함 생성")
    public void create_with_period_ok() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        Color color = Color.create("RED", "FF0000");
        Period period = Period.of(startDate, endDate, null);

        //when
        PersonalProject project = PersonalProject.createWithPeriod(
                color,
                member,
                 "   나의 프로젝트    ",
                Status.PLANNING,
                period,
                "   잘해보자구~");

        //then
        assertThat(project.getName()).isEqualTo("나의 프로젝트");
        assertThat(project.getStatus()).isEqualTo(Status.PLANNING);
        assertThat(project.getPeriod().startDate()).isEqualTo(startDate);
        assertThat(project.getPeriod().endDate()).isEqualTo(endDate);
        assertThat(project.getPeriod().actualEndDate()).isNull();
        assertThat(project.getDescription()).isEqualTo("잘해보자구~");
        assertThat(project.getColor().getHexCode()).isEqualTo("FF0000");
        assertThat(project.getColor().getName()).isEqualTo("RED");
    }

    @Test
    @DisplayName("검증 실패: color, name, status가 null 이면 BaseException 반환")
    public void create_fail_validation() throws Exception {
        //given
        Color color = Color.create("RED", "FF0000");

        //then
        //color null
        assertThatThrownBy(() -> PersonalProject.create(
                null, member,"name", Status.PLANNING, "description"
        )).isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.MISSING_COLOR_FOR_PROJECT.getMessage());

        //name empty
        assertThatThrownBy(() -> PersonalProject.create(
                color, member,"", Status.PLANNING, "description"
        )).isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.MISSING_TITLE_FOR_PROJECT.getMessage());

        //name toLong
        String longName = "a".repeat(101);
        assertThatThrownBy(() -> PersonalProject.create(
                color, member,longName, Status.PLANNING,
                "description"
        )).isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.TITLE_EXCEEDS_LIMIT_FOR_PROJECT.getMessage());

        //status null
        assertThatThrownBy(() -> PersonalProject.create(
                color, member,"name", null, "description"
        )).isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.MISSING_STATUS_FOR_PROJECT.getMessage());
    }

    @Test
    @DisplayName("수정 로직: color/name/status/period/description")
    public void change_fields() throws Exception {
        //given
        Color color = Color.create("RED", "FF0000");
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        LocalDate actualEndDate = LocalDate.of(2025, 12, 31);
        Period period = Period.of(startDate, endDate, actualEndDate);
        PersonalProject project = PersonalProject.createWithPeriod(
                color,
                member,
                "   나의 프로젝트    ",
                Status.PLANNING,
                period,
                "   잘해보자구~"
        );

        //when
        project.changeColor(Color.create("BLUE", "0000FF"));
        project.changeName(" 변경됨      ");
        project.changeStatus(Status.COMPLETED);
        project.changePeriod(Period.of(startDate.plusDays(1), endDate.minusDays(5), actualEndDate.minusDays(5)));
        project.changeDescription("  내용이 변경 되었어요  ");

        //then
        assertThat(project.getColor().getName()).isEqualTo("BLUE");
        assertThat(project.getColor().getHexCode()).isEqualTo("0000FF");
        assertThat(project.getName()).isEqualTo("변경됨");
        assertThat(project.getStatus()).isEqualTo(Status.COMPLETED);
        assertThat(project.getPeriod().startDate()).isEqualTo(LocalDate.of(2025, 1, 2));
        assertThat(project.getPeriod().endDate()).isEqualTo(LocalDate.of(2025, 12, 26));
        assertThat(project.getPeriod().actualEndDate()).isEqualTo(LocalDate.of(2025, 12, 26));
        assertThat(project.getDescription()).isEqualTo("내용이 변경 되었어요");
    }

    @Test
    @DisplayName("수정 실패: 잘못된 값이 들어오면 BaseException 반환")
    public void change_fail() throws Exception {
        //given
        Color color = Color.create("RED", "FF0000");
        PersonalProject project = PersonalProject.create(
                color,
                member,
                "name",
                Status.PLANNING,
                "description"
                );

        //then
        assertThatThrownBy(() -> project.changeColor(null))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_COLOR_FOR_PROJECT.getMessage());

        assertThatThrownBy(() -> project.changeName(""))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_TITLE_FOR_PROJECT.getMessage());

        String longName = "a".repeat(101);
        assertThatThrownBy(() -> project.changeName(longName))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.TITLE_EXCEEDS_LIMIT_FOR_PROJECT.getMessage());

        assertThatThrownBy(() -> project.changeStatus(null))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_STATUS_FOR_PROJECT.getMessage());
    }
}
