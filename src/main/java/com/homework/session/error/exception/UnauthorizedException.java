package com.homework.session.error.exception;

import com.homework.session.error.ErrorCode;

public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
