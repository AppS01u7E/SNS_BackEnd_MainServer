package com.jinwoo.snsbackend_mainserver.domain.soom.dto.request;


import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Setter
@Getter
public class GeneSoomRequest {
    private String title;
    private String info;
    private SoomType soomType;
    private String representitiveId;

}
