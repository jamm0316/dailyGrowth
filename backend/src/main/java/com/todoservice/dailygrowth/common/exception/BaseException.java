package com.todoservice.dailygrowth.common.exception;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final BaseResponseStatus status;

    public BaseException(BaseResponseStatus baseResponseStatus) {
        super(baseResponseStatus.getMessage());
        this.status = baseResponseStatus;
    }
}
