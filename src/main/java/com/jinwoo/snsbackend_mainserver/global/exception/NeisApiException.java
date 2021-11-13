package com.jinwoo.snsbackend_mainserver.global.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NeisApiException extends RuntimeException{
    public NeisApiException(String e){
        super(e);
    }
}
