package com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request;


import lombok.Getter;

@Getter
public class IdentifyScheInfoBlockRequest {
    private int grade;
    private int classNum;
    private int period;
    private int date;
}
