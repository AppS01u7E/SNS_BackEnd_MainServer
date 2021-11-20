package com.jinwoo.snsbackend_mainserver.domain.soom.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditCommentRequest {
    private Long commentId;
    private Long noticeId;
    private String message;
}
