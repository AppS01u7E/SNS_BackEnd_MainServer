package com.jinwoo.snsbackend_mainserver.domain.soom.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class NoticeFileUploadRequest {

    private List<MultipartFile> files;
}
