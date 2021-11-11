package com.jinwoo.snsbackend_mainserver.domain.auth.exception;

import com.jinwoo.snsbackend_mainserver.global.exception.ErrorCode;
import com.jinwoo.snsbackend_mainserver.global.exception.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends GlobalException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
