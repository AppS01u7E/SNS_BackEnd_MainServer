package com.jinwoo.snsbackend_mainserver.domain.auth.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class School {
    //학교정보 및 학반번호까지 모두 포함
    private String schoolName;
    private String areaCode;
    private String scoolCode;
    private int grade;
    private int classNum;
}
