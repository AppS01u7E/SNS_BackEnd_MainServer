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
    DATA_CANNOT_BRING("입출력 과정에서 오류가 발생하였습니다.", HttpStatus.BAD_GATEWAY),
    LOWER_AUTHENTICATION("작업을 수행하기에는 너무 낮은 권한입니다.", HttpStatus.BAD_REQUEST),
    SOOM_NOT_FOUND("그룹을 찾지 못하였습니다.", HttpStatus.NOT_FOUND),
    UPLOADINGERROR("파일을 업로드 하던 도중 에러가 발생하였습니다.", HttpStatus.BAD_GATEWAY),
    NOTICE_NOT_FOUND("공지를 찾지 못하였습니다.", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND("댓글을 찾지 못하였습니다.", HttpStatus.NOT_FOUND),
    INVALID_CODE("잘못된 코드입니다.", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_TYPE("올바른 이메일 타입이 아닙니다.", HttpStatus.BAD_REQUEST),
    NOT_SCHOOL_EMAIL("학교 소유 이메일이 아닙니다.", HttpStatus.BAD_REQUEST),
    INVALID_JOIN_CODE("올바른 코드 값이 아닙니다.", HttpStatus.BAD_REQUEST),
    TOO_MANY_FILES_EXISTS("파일이 이미 너무 많이 존재합니다.", HttpStatus.BAD_REQUEST),
    NOT_EXISTS_IN_LIST("리스트 내에 값이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_EXISTS_IN_LIST("값이 이미 리스트 내에 존재합니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;
}
