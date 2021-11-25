package com.jinwoo.snsbackend_mainserver.domain.soom.dto.response;


import com.jinwoo.snsbackend_mainserver.domain.soom.entity.Comment;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public class DetailNoticeResponse {

    private Long id;

    private String title;
    private String info;

    private List<String> files;

    private String roomId;

    private List<Comment> comments;

    private LocalDate createdAt;

    private LocalDate updatedAt;

}
