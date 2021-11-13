package com.jinwoo.snsbackend_mainserver.domain.auth.exception;

import com.jinwoo.snsbackend_mainserver.global.exception.base.ErrorCode;
import com.jinwoo.snsbackend_mainserver.global.exception.base.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MemberNotFoundException extends GlobalException {
    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
