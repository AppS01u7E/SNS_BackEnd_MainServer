package com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditScheduleRequest {
    private Long memoId;
    private String title;
    private String info;
}
