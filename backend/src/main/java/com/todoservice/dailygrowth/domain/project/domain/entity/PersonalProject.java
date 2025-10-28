package com.todoservice.dailygrowth.domain.project.domain.entity;

import com.todoservice.dailygrowth.common.enums.Status;
import com.todoservice.dailygrowth.domain.color.entity.Color;
import com.todoservice.dailygrowth.domain.member.domain.entity.Member;
import com.todoservice.dailygrowth.domain.project.domain.vo.Period;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("PERSONAL")
public class PersonalProject extends Project{
    public PersonalProject(Color color, Member member, String name, Status status, Period period, String description) {
        super(color, member, name, status, period, description);
    }

    /**
     * ===== 개인 프로젝트 고유 메서드 ====
     **/
    static public PersonalProject create(Color color, Member member, String name, Status status, String description) {
        return new PersonalProject(color, member, name, status, null, description);
    }

    static public PersonalProject createWithPeriod(Color color, Member member, String name, Status status,
                                            Period period, String description) {
        return new PersonalProject(color, member, name, status, period, description);
    }
}
