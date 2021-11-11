package com.jinwoo.snsbackend_mainserver.domain.auth.entity;


import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class School {
    //학교정보 및 학반번호까지 모두 포함
    private String schoolName;
    private int scoolCode;
    private int grade;
    private int classNum;
    //1학년 3반 == 103
}
