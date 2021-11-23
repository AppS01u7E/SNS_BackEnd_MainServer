package com.jinwoo.snsbackend_mainserver.domain.auth.payload.request;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Gender;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.School;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class EditMypageRequest {

    private Gender gender;

    private LocalDate birth;

    private School school;

    private int grade;

    private int classNum;

    private String info;
}
