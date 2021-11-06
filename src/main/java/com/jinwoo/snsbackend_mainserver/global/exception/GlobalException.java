package com.jinwoo.snsbackend_mainserver.global.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class GlobalException extends RuntimeException{
    private ErrorCode errorCode;

    public GlobalException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
