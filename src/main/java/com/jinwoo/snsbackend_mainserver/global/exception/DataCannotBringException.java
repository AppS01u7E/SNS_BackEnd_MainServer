package com.jinwoo.snsbackend_mainserver.global.exception;

import com.jinwoo.snsbackend_mainserver.global.exception.base.ErrorCode;
import com.jinwoo.snsbackend_mainserver.global.exception.base.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class DataCannotBringException extends GlobalException {
    public DataCannotBringException(){
        super(ErrorCode.DATA_CANNOT_BRING);
    }
}
