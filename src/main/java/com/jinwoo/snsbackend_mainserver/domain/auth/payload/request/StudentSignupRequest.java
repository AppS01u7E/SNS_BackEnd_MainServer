package com.jinwoo.snsbackend_mainserver.domain.auth.payload.request;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Gender;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Role;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.School;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.lang.Nullable;

import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class StudentSignupRequest {

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

    private String email;

    private String teacherId;

}
