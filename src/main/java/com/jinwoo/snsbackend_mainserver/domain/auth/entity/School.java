package com.jinwoo.snsbackend_mainserver.domain.auth.entity;


import javax.persistence.Embeddable;

@Embeddable
public class School {
    //학교정보 및 학반번호까지 모두 포함
    private String name;
    private int id;
    private int grade;
    private int academicInfo;
    //1학년 3반 == 103
}
