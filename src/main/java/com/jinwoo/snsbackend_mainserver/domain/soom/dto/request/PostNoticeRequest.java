package com.jinwoo.snsbackend_mainserver.domain.soom.dto.request;


import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class PostNoticeRequest {
    @NotEmpty(message = "soomId는 비어선 안됩니다.")
    private String soomId;
    @NotEmpty(message = "제목은 비어서는 안됩니다.")
    private String title;
    private String info;

}
