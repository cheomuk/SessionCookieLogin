package com.homework.session.error.exception;

import com.homework.session.error.ErrorCode;

public class InternerServerException extends BusinessException {

    public InternerServerException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
