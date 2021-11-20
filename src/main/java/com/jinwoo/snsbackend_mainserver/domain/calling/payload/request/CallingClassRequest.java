package com.jinwoo.snsbackend_mainserver.domain.calling.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallingClassRequest {
    private int grade;
    private String title;
    private String message;
}
