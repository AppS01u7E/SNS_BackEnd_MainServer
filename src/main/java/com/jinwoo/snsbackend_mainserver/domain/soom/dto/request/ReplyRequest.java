package com.jinwoo.snsbackend_mainserver.domain.soom.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyRequest {
    private Long commentId;
    private String content;
}
