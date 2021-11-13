package com.jinwoo.snsbackend_mainserver.domain.schoolInfo.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class SchoolInfoException extends RuntimeException{
    public SchoolInfoException(String message){
        super(message);
    }
}
