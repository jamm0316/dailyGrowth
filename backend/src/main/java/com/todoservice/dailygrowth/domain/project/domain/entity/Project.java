package com.todoservice.dailygrowth.domain.project.domain.entity;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.enums.Status;
import com.todoservice.dailygrowth.common.enums.Visibility;
import com.todoservice.dailygrowth.common.exception.BaseException;
import com.todoservice.dailygrowth.common.superEntity.SuperEntity;
import com.todoservice.dailygrowth.domain.color.entity.Color;
import com.todoservice.dailygrowth.domain.member.domain.entity.Member;
import com.todoservice.dailygrowth.domain.project.domain.vo.ChallengeDetails;
import com.todoservice.dailygrowth.domain.project.domain.vo.Period;
import com.todoservice.dailygrowth.domain.project.domain.vo.ProjectType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends SuperEntity {
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

    @NotNull(message = "Project Type은 필수입니다.")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30) default 'PERSONAL'")
    private ProjectType projectType;

    @Embedded
    private Period period;

    private String description;

    @NotNull(message = "공개 여부는 필수입니다.")
    @Column(columnDefinition = "boolean default true")
    private Boolean isPublic;

    @NotNull(message = "공개 범위는 필수입니다.")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'PRIVATE'")
    private Visibility visibility;

    @Embedded
    private ChallengeDetails challengeDetails;

    public Project(Color color,
                   Member member,
                   String name,
                   Status status,
                   ProjectType projectType,
                   Period period,
                   String description,
                   Boolean isPublic,
                   Visibility visibility,
                   ChallengeDetails challengeDetails) {

        validateDomainInvariants(color, member, name, status, projectType, period, isPublic, visibility, challengeDetails);

        this.color = color;
        this.member = member;
        this.name = name.trim();
        this.status = status;
        this.projectType = projectType;
        this.period = period;
        this.description = description != null ? description.trim() : null;
        this.isPublic = isPublic;
        this.visibility = visibility;
        this.challengeDetails = challengeDetails;
    }

    private void validateDomainInvariants(Color color,
                                          Member member,
                                          String name,
                                          Status status,
                                          ProjectType projectType,
                                          Period period,
                                          Boolean isPublic,
                                          Visibility visibility,
                                          ChallengeDetails challengeDetails) {

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

        if (projectType == null) {
            throw new BaseException(BaseResponseStatus.MISSING_PROJECT_TYPE_FOR_PROJECT);
        }

        if (isPublic == null) {
            throw new BaseException(BaseResponseStatus.MISSING_IS_PUBLIC_FOR_PROJECT);
        }

        if (visibility == null) {
            throw new BaseException(BaseResponseStatus.MISSING_VISIBILITY_FOR_PROJECT);
        }

        if (projectType == ProjectType.CHALLENGE) {
            if (period == null || period.isNull()) {
                throw new BaseException(BaseResponseStatus.CHALLENGE_PERIOD_UNDEFINED);
            }

            if (challengeDetails == null || challengeDetails.isNull()) {
                throw new BaseException(BaseResponseStatus.CHALLENGE_DETAILS_NOT_ALLOWED_FOR_NON_CHALLENGE);
            }
        }
    }

    public static Project create(Color color, Member member, String name, Status status,
                                 String description, Boolean isPublic, Visibility visibility) {
        return new Project(color, member, name, status, ProjectType.PERSONAL, null, description, isPublic, visibility, null);
    }

    public static Project createWithPeriod(Color color, Member member, String name, Status status,
                                           Period period, String description, Boolean isPublic, Visibility visibility) {
        return new Project(color, member, name, status, ProjectType.PERSONAL, period, description, isPublic, visibility, null);
    }

    public static Project createChallenge(Color color, Member member, String name, Status status,
                                              Period period, String description, Boolean isPublic, Visibility visibility,
                                              ChallengeDetails challengeDetails) {
        return new Project(color, member, name, status, ProjectType.CHALLENGE, period, description, isPublic, visibility, challengeDetails);
    }

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

    public void changeProjectType(ProjectType projectType) {
        if (projectType == null) {
            throw new BaseException(BaseResponseStatus.MISSING_PROJECT_TYPE_FOR_PROJECT);
        }
        this.projectType = projectType;
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

    public void changeIsPublic(Boolean isPublic) {
        if (isPublic == null) {
            throw new BaseException(BaseResponseStatus.MISSING_IS_PUBLIC_FOR_PROJECT);
        }
        this.isPublic = isPublic;
    }

    public void changeVisibility(Visibility visibility) {
        if (visibility == null) {
            throw new BaseException(BaseResponseStatus.MISSING_VISIBILITY_FOR_PROJECT);
        }
        this.visibility = visibility;
    }

    public void increaseParticipant() {
        this.challengeDetails = this.challengeDetails.increaseParticipant();
    }

    public void decreaseParticipant() {
        this.challengeDetails = this.challengeDetails.decreaseParticipant();
    }
}
