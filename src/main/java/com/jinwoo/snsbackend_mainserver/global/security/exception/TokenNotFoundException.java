package com.jinwoo.snsbackend_mainserver.global.security.exception;

import com.jinwoo.snsbackend_mainserver.global.exception.ErrorCode;
import com.jinwoo.snsbackend_mainserver.global.exception.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TokenNotFoundException extends GlobalException {
    public TokenNotFoundException() {
        super(ErrorCode.TOKEN_NOT_FOUND);
    }
}
