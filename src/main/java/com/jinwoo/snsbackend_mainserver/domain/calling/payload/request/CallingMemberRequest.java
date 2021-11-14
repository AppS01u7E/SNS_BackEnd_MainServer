package com.jinwoo.snsbackend_mainserver.domain.calling.payload.request;


import lombok.Getter;
import lombok.Setter;

@Getter
public class CallingMemberRequest {
    private String receiverId;
    private String message;
}
