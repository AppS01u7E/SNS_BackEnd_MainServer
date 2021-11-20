package com.jinwoo.snsbackend_mainserver.domain.soom.dto.request;

import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProfilephotosRequest {
    private List<MultipartFile> profiles;
}
