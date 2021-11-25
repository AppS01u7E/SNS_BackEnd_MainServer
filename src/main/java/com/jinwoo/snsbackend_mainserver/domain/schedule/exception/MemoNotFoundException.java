package com.jinwoo.snsbackend_mainserver.domain.schedule.exception;

import com.jinwoo.snsbackend_mainserver.global.exception.base.ErrorCode;
import com.jinwoo.snsbackend_mainserver.global.exception.base.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemoNotFoundException extends GlobalException {
    public MemoNotFoundException(){
        super(ErrorCode.NOT_FOUND_SCHEDULE_INFO);
    }
}
