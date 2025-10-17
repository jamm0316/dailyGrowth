package com.todoservice.dailygrowth.domain.project.domain.vo;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.exception.BaseException;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public record Period(
        LocalDate startDate,
        LocalDate endDate,
        LocalDate actualEndDate
) {
    public Period  {
        validationPeriod(startDate, endDate, actualEndDate);
    }

    private void validationPeriod(LocalDate startDate, LocalDate endDate, LocalDate actualEndDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new BaseException(BaseResponseStatus.END_DATE_BEFORE_START_DATE);
        }

        if (startDate != null && actualEndDate != null && actualEndDate.isBefore(startDate)) {
            throw new BaseException(BaseResponseStatus.ACTUAL_END_DATE_BEFORE_START_DATE);
        }
    }

    public static Period noPeriod() {
        return new Period(null, null, null);
    }

    public static Period of (LocalDate startDate, LocalDate endDate, LocalDate actualEndDate) {
        return (actualEndDate == null)
                ? new Period(startDate, endDate, null)
                : new Period(startDate, endDate, actualEndDate);
    }

    public boolean isNull() {
        return startDate == null && endDate == null && actualEndDate == null;
    }
}