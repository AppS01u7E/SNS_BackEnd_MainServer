package com.jinwoo.snsbackend_mainserver.domain.soom.exception;

import com.jinwoo.snsbackend_mainserver.global.exception.base.ErrorCode;
import com.jinwoo.snsbackend_mainserver.global.exception.base.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RepCannotOutException extends GlobalException {
    public RepCannotOutException() {
        super(ErrorCode.REP_CANNOT_OUT);
    }
}
