package com.todoservice.dailygrowth.domain.task.domain.vo;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.exception.BaseException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
public record Schedule(
        LocalDate startDate,
        LocalTime startTime,
        @Column(columnDefinition = "boolean default false")
        Boolean startTimeEnabled,

        LocalDate dueDate,
        LocalTime dueTime,
        @Column(columnDefinition = "boolean default false")
        Boolean dueTimeEnabled
) {
        public Schedule {
                startTimeEnabled = startTimeEnabled == null ? false : startTimeEnabled;
                dueTimeEnabled = dueTimeEnabled == null ? false : dueTimeEnabled;

                validationSchedule(startDate, startTime, startTimeEnabled, dueDate, dueTime, dueTimeEnabled);
        }

        private void validationSchedule(LocalDate startDate,
                                       LocalTime startTime,
                                       Boolean startTimeEnabled,
                                       LocalDate dueDate,
                                       LocalTime dueTime,
                                       Boolean dueTimeEnabled) {
                if (startDate != null && dueDate != null && dueDate.isBefore(startDate)) {
                        throw new BaseException(BaseResponseStatus.INVALID_DATE_ORDER);
                }

                if (Boolean.TRUE.equals(startTimeEnabled) && startDate == null) {
                        throw new BaseException(BaseResponseStatus.MISSING_START_DATE_FOR_TIME);
                }

                if (Boolean.TRUE.equals(dueTimeEnabled) && dueDate == null) {
                        throw new BaseException(BaseResponseStatus.MISSING_DUE_DATE_FOR_TIME);
                }

                if (Boolean.TRUE.equals(startTimeEnabled) && startTime == null) {
                        throw new BaseException(BaseResponseStatus.MISSING_START_TIME_VALUE);
                }

                if (Boolean.TRUE.equals(dueTimeEnabled) && dueTime == null) {
                        throw new BaseException(BaseResponseStatus.MISSING_DUE_TIME_VALUE);
                }

                if (startDate != null && dueDate != null && startDate.equals(dueDate)
                        && Boolean.TRUE.equals(startTimeEnabled) && Boolean.TRUE.equals(dueTimeEnabled &&
                        startTime != null && dueTime != null && dueTime.isBefore(startTime))) {
                        throw new BaseException(BaseResponseStatus.INVALID_TIME_ORDER_SAME_DATE);
                }
        }

        public static Schedule noSchedule() {
                return new Schedule(null, null, null, null, null, null);
        }

        public static Schedule of (LocalDate startDate, LocalTime startTime, Boolean startTimeEnabled,
                LocalDate dueDate, LocalTime dueTime, Boolean dueTimeEnabled) {
                return new Schedule(startDate, startTime, startTimeEnabled,
                        dueDate, dueTime, dueTimeEnabled);
        }

        public boolean isNull() {
                return startDate == null && startTime == null && startTimeEnabled == null
                        && dueDate == null && dueTime == null && dueTimeEnabled == null;
        }
}
