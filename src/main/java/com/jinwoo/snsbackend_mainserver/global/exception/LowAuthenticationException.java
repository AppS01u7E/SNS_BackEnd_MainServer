package com.jinwoo.snsbackend_mainserver.global.exception;

import com.jinwoo.snsbackend_mainserver.global.exception.base.ErrorCode;
import com.jinwoo.snsbackend_mainserver.global.exception.base.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LowAuthenticationException extends GlobalException {
    public LowAuthenticationException(){
        super(ErrorCode.LOWER_AUTHENTICATION);
    }
}
