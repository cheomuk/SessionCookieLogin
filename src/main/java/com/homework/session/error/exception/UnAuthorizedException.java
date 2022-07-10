package com.homework.session.error.exception;

import com.homework.session.error.ErrorCode;

public class UnAuthorizedException extends BusinessException {

    public UnAuthorizedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
