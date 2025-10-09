package com.todoservice.greencatsoftware.domain.project.application;

import com.todoservice.greencatsoftware.config.security.principal.CustomUser;
import com.todoservice.greencatsoftware.domain.color.entity.Color;
import com.todoservice.greencatsoftware.domain.color.application.ColorService;
import com.todoservice.greencatsoftware.domain.member.application.MemberService;
import com.todoservice.greencatsoftware.domain.member.domain.entity.Member;
import com.todoservice.greencatsoftware.domain.project.domain.entity.Project;
import com.todoservice.greencatsoftware.domain.project.domain.vo.Period;
import com.todoservice.greencatsoftware.domain.project.presentation.dto.ProjectCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectFactory {
    private final MemberService memberService;
    private final ColorService colorService;

    public Project createProject(User user, ProjectCreateRequest request) {
        log.info("[PROJECT_FACTORY]: factory.enter");
        long userId = Long.parseLong(user.getUsername());
        log.info("[PROJECT_FACTORY]: user.Id:{}", userId);
        Member member = memberService.getMemberByIdOrThrow(userId);
        log.info("[PROJECT_FACTORY]: getMemberId:{}", member.getId());
        Color color = colorService.getColorByIdOrThrow(request.colorId());
        Period period = Period.of(request.period().startDate(), request.period().endDate(),
                                  request.period().actualEndDate());
        Project project = (period.isNull())
                ? Project.create(color, member, request.name(), request.status(),
                request.description(), request.isPublic(), request.visibility())

                : Project.createWithPeriod(color, member, request.name(), request.status(),
                period, request.description(), request.isPublic(), request.visibility());

        log.info("[PROJECT_FACTORY]: project.id:{}, project.name:{}", project.getId(), project.getName());
        return project;
//                (period.isNull())
//                ? Project.create(color, member, request.name(), request.status(),
//                request.description(), request.isPublic(), request.visibility())
//
//                : Project.createWithPeriod(color, member, request.name(), request.status(),
//                period, request.description(), request.isPublic(), request.visibility());
    }
}