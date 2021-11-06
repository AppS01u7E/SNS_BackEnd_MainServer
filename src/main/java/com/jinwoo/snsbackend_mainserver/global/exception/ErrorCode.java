package com.jinwoo.snsbackend_mainserver.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;



@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_TOKEN("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    MEMBER_NOT_FOUND("사용자를 찾을 수 업습니다.", HttpStatus.NOT_FOUND),
    TOKEN_NOT_FOUND("토큰을 찾지 못하였습니다.", HttpStatus.NOT_FOUND);


    private final String message;
    private final HttpStatus httpStatus;
}
