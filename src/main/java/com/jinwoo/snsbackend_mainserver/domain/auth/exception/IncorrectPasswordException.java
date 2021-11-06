package com.jinwoo.snsbackend_mainserver.domain.auth.exception;

import com.jinwoo.snsbackend_mainserver.global.exception.ErrorCode;
import com.jinwoo.snsbackend_mainserver.global.exception.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IncorrectPassword extends GlobalException {
    public IncorrectPassword() {
        super(ErrorCode.INCORRECT_PASSWORD);
    }
}
