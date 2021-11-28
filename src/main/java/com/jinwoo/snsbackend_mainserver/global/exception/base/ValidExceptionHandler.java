package com.jinwoo.snsbackend_mainserver.global.exception.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@ControllerAdvice
@RestController
@Slf4j
public class ValidExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.toString());
        return ResponseEntity.badRequest().body(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }
    @ExceptionHandler(GlobalException.class)
    protected ResponseEntity<?> globalExceptionHandler(GlobalException e){
        log.error(e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getMessage());
    }



}