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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
public class StudentSignupRequest {
    @Email(message = "id 입력값은 Email 타입이여야합니다.")
    @Pattern(regexp = "^[a-z0-9A-Z._-]*@dsm.hs.kr$", message = "dsm.hs 이메일이 아닙니다.")
    private String id;
    @NotEmpty(message = "password에 값이 존재하지 않습니다.")
    private String password;

    private Gender gender;


    private LocalDate birth;

    private School school;
    private int grade;
    private int classNum;

    @NotEmpty(message = "name에 값이 존재하지 않습니다.")
    private String name;
    private Role role;

    @NotEmpty(message = "teacherId에 값이 존재하지 않습니다.")
    private String teacherId;

}
