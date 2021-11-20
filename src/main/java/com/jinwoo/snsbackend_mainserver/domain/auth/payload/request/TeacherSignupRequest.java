package com.jinwoo.snsbackend_mainserver.domain.auth.payload.request;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Gender;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;


@Getter
@Setter
public class TeacherSignupRequest {

    private String id;
    private String password;

    private Gender gender;

    private LocalDate birth;

    private String schoolName;
    private String areaCode;
    private String scoolCode;

    private int grade;
    private int classNum;


    private String name;
    private Role role;

    private String teacherKey;

}
