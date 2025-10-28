package com.todoservice.dailygrowth.domain.project.domain.entity;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.enums.Status;
import com.todoservice.dailygrowth.common.exception.BaseException;
import com.todoservice.dailygrowth.common.superEntity.SuperEntity;
import com.todoservice.dailygrowth.domain.color.entity.Color;
import com.todoservice.dailygrowth.domain.member.domain.entity.Member;
import com.todoservice.dailygrowth.domain.project.domain.vo.Period;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "project_type")
public abstract class Project extends SuperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "color_id는 필수 입니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "color_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_PROJECT_COLOR")
    )
    private Color color;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_PROJECT_MEMBER")
    )
    private Member member;

    @NotNull(message = "프로젝트 이름은 필수 입니다.")
    private String name;

    @NotNull(message = "상태값은 필수 입니다.")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30) default 'PLANNING'")
    private Status status;

    @Embedded
    private Period period;

    private String description;

    public Project(Color color,
                   Member member,
                   String name,
                   Status status,
                   Period period,
                   String description) {

        validateDomainInvariants(color, member, name, status);

        this.color = color;
        this.member = member;
        this.name = name.trim();
        this.status = status;
        this.period = period;
        this.description = description != null ? description.trim() : null;
    }

    private void validateDomainInvariants(Color color,
                                          Member member,
                                          String name,
                                          Status status) {

        if (color == null) {
            throw new BaseException(BaseResponseStatus.MISSING_COLOR_FOR_PROJECT);
        }

        if (member == null) {
            throw new BaseException(BaseResponseStatus.MISSING_MEMBER_FOR_PROJECT);
        }

        if (name == null || name.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.MISSING_TITLE_FOR_PROJECT);
        }

        if (name.length() > 100) {
            throw new BaseException(BaseResponseStatus.TITLE_EXCEEDS_LIMIT_FOR_PROJECT);
        }

        if (status == null) {
            throw new BaseException(BaseResponseStatus.MISSING_STATUS_FOR_PROJECT);
        }
    }

    /**
     * ========== 공통 변경 메서드 ==========
     **/
    public void changeColor(Color color) {

        if (color == null) {
            throw new BaseException(BaseResponseStatus.MISSING_COLOR_FOR_PROJECT);
        }
        this.color = color;
    }

    public void changeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.MISSING_TITLE_FOR_PROJECT);
        }

        if (name.length() > 100) {
            throw new BaseException(BaseResponseStatus.TITLE_EXCEEDS_LIMIT_FOR_PROJECT);
        }

        this.name = name.trim();
    }

    public void changeStatus(Status status) {
        if (status == null) {
            throw new BaseException(BaseResponseStatus.MISSING_STATUS_FOR_PROJECT);
        }
        this.status = status;
    }

    public void changePeriod(Period period) {
        if (period.isNull()) {
            this.period = Period.noPeriod();
        } else {
            this.period = period;
        }
    }

    public void changeDescription(String description) {
        if (description != null && !description.isBlank()) {
            this.description = description.trim();
        } else {
            this.description = null;
        }
    }
}
