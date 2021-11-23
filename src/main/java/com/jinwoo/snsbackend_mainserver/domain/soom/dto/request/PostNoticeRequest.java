package com.jinwoo.snsbackend_mainserver.domain.soom.dto.request;


import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class PostNoticeRequest {
    @NotEmpty(message = "soomId는 비어선 안됩니다.")
    private String soomId;
    @NotEmpty(message = "제목은 비어서는 안됩니다.")
    @Size(max = 30, message = "제목은 30")
    private String title;
    @Size(max = 300, message = "내용은 300자 이내여야합니다.")
    private String info;

}
