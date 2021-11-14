package com.jinwoo.snsbackend_mainserver.global.exception.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;



@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_TOKEN("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    MEMBER_NOT_FOUND("사용자를 찾을 수 업습니다.", HttpStatus.NOT_FOUND),
    TOKEN_NOT_FOUND("토큰을 찾지 못하였습니다.", HttpStatus.NOT_FOUND),
    MEMBER_ALREADY_EXISTS_WITH_ID("해당 Id의 사용자가 이미 존재합니다.", HttpStatus.CONFLICT),
    INCORRECT_PASSWORD("유효하지 않은 패스워드 입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_SCHEDULE_INFO("해당 시간의 메모가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    DATA_CANNOT_BRING("데이터를 가져올 수 업습니다.", HttpStatus.BAD_GATEWAY),
    LOWER_AUTHENTICATION("작업을 수행하기에는 너무 낮은 권한입니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;
}
