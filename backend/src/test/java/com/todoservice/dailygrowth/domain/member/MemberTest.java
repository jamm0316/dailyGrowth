package com.todoservice.dailygrowth.domain.member;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.exception.BaseException;
import com.todoservice.dailygrowth.domain.auth.domain.oauth.vo.OAuth2Provider;
import com.todoservice.dailygrowth.domain.member.domain.entity.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class MemberTest {

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("member 생성 성공")
    public void create_ok() throws Exception {
        Member member = Member.create(
                "test@test.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr123",
                "Test1234@!#$",
                null,
                "testName");

        //then
        assertThat(member.getEmail()).isEqualTo("test@test.com");
        assertThat(member.getPassword()).isEqualTo("Test1234@!#$");
        assertThat(member.getName()).isEqualTo("testName");
        assertThat(member.getProfileImageUrl()).isNull();
    }

    @Test
    @DisplayName("email이 null/blank면 도메인 레벨에서 BaseException 반환")
    public void create_fail_email() throws Exception {
        //then
        assertThatThrownBy(() -> Member.create(
                null,
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr123",
                "Test1234@!#$",
                null,
                "testName"))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_EMAIL_FOR_MEMBER.getMessage());

        assertThatThrownBy(() -> Member.create(
                "    ",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr123",
                "Test1234@!#$",
                null,
                "testName"))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_EMAIL_FOR_MEMBER.getMessage());
    }

    @Test
    @DisplayName("password가 null/blank면 도메인 레벨에서 BaseException 반환")
    public void create_fail_password() throws Exception {
        //then
        assertThatThrownBy(() -> Member.create(
                "email@email.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr123",
                null,
                null,
                "testName"))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_PASSWORD_FOR_MEMBER.getMessage());

        assertThatThrownBy(() -> Member.create(
                "email@email.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr123",
                "    ",
                null,
                "testName"))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_PASSWORD_FOR_MEMBER.getMessage());
    }

    @Test
    @DisplayName("name이 null/blank면 도메인 레벨에서 BaseException 반환")
    public void create_fail_name() throws Exception {
        //then
        assertThatThrownBy(() -> Member.create(
                "email@email.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr123",
                "Test1234@!#$",
                null,
                null))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_NAME_FOR_MEMBER.getMessage());

        assertThatThrownBy(() -> Member.create(
                "email@email.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr123",
                "Test1234@!#$",
                null,
                "     "))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_NAME_FOR_MEMBER.getMessage());
    }

    @Test
    @DisplayName("email 변경 성공")
    public void change_ok_email() throws Exception {
        //given
        Member member = Member.create(
                "email@email.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr123",
                "Test1234@!#$",
                null,
                "testName");

        //when
        member.changeEmail("newEmail@email.com");

        //then
        assertThat(member.getEmail()).isEqualTo("newEmail@email.com");
    }

    @Test
    @DisplayName("eamil 변경 시 null/blank 시 BaseException")
    public void change_fail_email_null_or_blank() throws Exception {
        //given
        Member member = Member.create(
                "email@email.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr123",
                "Test1234@!#$",
                null,
                "testName");

        //then
        assertThatThrownBy(() -> member.changeEmail(null))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_EMAIL_FOR_MEMBER.getMessage());

        assertThatThrownBy(() -> member.changeEmail("   "))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_EMAIL_FOR_MEMBER.getMessage());
    }

    @Test
    @DisplayName("password 변경 성공")
    public void change_ok_password() throws Exception {
        //given
        Member member = Member.create(
                "email@email.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr123",
                "Test1234@!#$",
                null,
                "testName");

        //when
        member.changePassword("NewPassword1234@");

        //then
        assertThat(member.getPassword()).isEqualTo("NewPassword1234@");
    }

    @Test
    @DisplayName("password 변경 시 null/blank면 BaseException")
    public void change_fail_password_null_or_blank() throws Exception {
        //given
        Member member = Member.create(
                "email@email.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr123",
                "Test1234@!#$",
                null,
                "testName");

        //then
        assertThatThrownBy(() -> member.changePassword(null))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_PASSWORD_FOR_MEMBER.getMessage());

        assertThatThrownBy(() -> member.changePassword("   "))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_PASSWORD_FOR_MEMBER.getMessage());
    }

    @Test
    @DisplayName("profileImageUrl 변경 성공")
    public void change_ok_profileImageUrl() throws Exception {
        //given
        Member member = Member.create(
                "email@email.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr123",
                "Test1234@!#$",
                null,
                "testName");

        //when
        member.changeProfileImageUrl("src/image/newImageUrl");

        //then
        assertThat(member.getProfileImageUrl()).isEqualTo("src/image/newImageUrl");

    }

    @Test
    @DisplayName("name 변경 성공")
    public void change_ok_name() throws Exception {
        //given
        Member member = Member.create(
                "email@email.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr123",
                "Test1234@!#$",
                null,
                "testName");

        //when
        member.changeName("NewName");

        //then
        assertThat(member.getName()).isEqualTo("NewName");
    }

    @Test
    @DisplayName("name 변경 시 null/blank면 BaseException")
    public void change_fail_name_null_or_blank() throws Exception {
        //given
        Member member = Member.create(
                "email@email.com",
                OAuth2Provider.KAKAO,
                "asevanoeqointqewr123",
                "Test1234@!#$",
                null,
                "testName");

        //then
        assertThatThrownBy(() -> member.changeName(null))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_NAME_FOR_MEMBER.getMessage());

        assertThatThrownBy(() -> member.changeName("    "))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_NAME_FOR_MEMBER.getMessage());
    }
}
