package com.jinwoo.snsbackend_mainserver.domain.calling.payload.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallingClassAndGradeRequest {

    private int grade;
    private int classNum;

    private String title;
    private String message;
}
