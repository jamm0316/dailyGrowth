package com.todoservice.dailygrowth.domain.project.application;

import com.todoservice.dailygrowth.domain.color.entity.Color;
import com.todoservice.dailygrowth.domain.color.application.ColorService;
import com.todoservice.dailygrowth.domain.member.application.MemberService;
import com.todoservice.dailygrowth.domain.member.domain.entity.Member;
import com.todoservice.dailygrowth.domain.project.domain.entity.PersonalProject;
import com.todoservice.dailygrowth.domain.project.domain.entity.Project;
import com.todoservice.dailygrowth.domain.project.domain.vo.Period;
import com.todoservice.dailygrowth.domain.project.presentation.dto.ProjectCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectFactory {
    private final MemberService memberService;
    private final ColorService colorService;

    public PersonalProject createProject(User user, ProjectCreateRequest request) {
        long userId = Long.parseLong(user.getUsername());
        Member member = memberService.getMemberByIdOrThrow(userId);
        Color color = colorService.getColorByIdOrThrow(request.colorId());
        Period period = Period.of(request.period().startDate(), request.period().endDate(),
                request.period().actualEndDate());

        return (period.isNull())
                ? PersonalProject.create(color, member, request.name(), request.status(),
                request.description())

                : PersonalProject.createWithPeriod(color, member, request.name(), request.status(),
                period, request.description());
    }
}