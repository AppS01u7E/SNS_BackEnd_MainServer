package com.jinwoo.snsbackend_mainserver.domain.soom.exception;

import com.jinwoo.snsbackend_mainserver.global.exception.base.ErrorCode;
import com.jinwoo.snsbackend_mainserver.global.exception.base.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundExcetion extends GlobalException {
    public CommentNotFoundExcetion() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}
