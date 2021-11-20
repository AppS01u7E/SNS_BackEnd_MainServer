package com.jinwoo.snsbackend_mainserver.domain.chatting.payload.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FileMessageRequest {
    @NotNull(message = "file 항목이 비었습니다.")
    private MultipartFile file;

}
