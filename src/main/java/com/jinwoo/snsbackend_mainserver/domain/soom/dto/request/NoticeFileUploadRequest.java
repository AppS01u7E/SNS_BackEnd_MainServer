package com.jinwoo.snsbackend_mainserver.domain.soom.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class NoticeFileUploadRequest {

    private List<MultipartFile> files;
}
