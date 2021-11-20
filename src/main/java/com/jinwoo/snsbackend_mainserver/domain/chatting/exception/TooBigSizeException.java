package com.jinwoo.snsbackend_mainserver.domain.chatting.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TooBigSizeException extends RuntimeException{
    public TooBigSizeException(long getSize, long size){
        super(getSize + "파일의 크기가 너무 큽니다. "+ size/1000000 + "mb 보다 작아야합니다.");
    }
}
