package com.jinwoo.snsbackend_mainserver.domain.schedule.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ScheduleException extends RuntimeException{
    public ScheduleException(String message){
        super(message);
    }
}
