package com.jinwoo.snsbackend_mainserver.global.sms;

import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SmsException extends RuntimeException {
    public SmsException(CoolsmsException e) {
        super(e.getMessage());
    }

}
