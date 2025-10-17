package com.todoservice.dailygrowth.domain.color.entity;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.exception.BaseException;
import com.todoservice.dailygrowth.common.superEntity.SuperEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Color extends SuperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull(message = "색상 이름은 필수입니다.")
    private String name;

    @Column(unique = true)
    @NotNull(message = "색상코드는 필수입니다.")
    private String hexCode;

    public Color(String name, String hexCode) {
        validateDomainInvariants(name, hexCode);
        this.name = name.trim().toUpperCase(Locale.ROOT);
        this.hexCode = hexCode.trim();
    }

    private void validateDomainInvariants(String name, String hexCode) {
        if (name == null || name.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.MISSING_NAME_FOR_COLOR);
        }

        if (hexCode == null || hexCode.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.MISSING_HEX_CODE_FOR_COLOR);
        }
    }

    public static Color create(String name, String hexCode) {
        return new Color(name, hexCode);
    }

    public void changeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.MISSING_NAME_FOR_COLOR);
        }
        this.name = name.trim();
    }

    public void changeHexCode(String hexCode) {
        if (hexCode == null || hexCode.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.MISSING_HEX_CODE_FOR_COLOR);
        }
        this.hexCode = hexCode.trim();
    }
}
