package com.jinwoo.snsbackend_mainserver.domain.auth.payload.request;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Gender;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Role;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.School;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
public class StudentSignupRequest {
    @Email(message = "id 입력값은 Email 타입이여야합니다.")
    @Pattern(regexp = "^[a-z0-9A-Z._-]*@dsm.hs.kr$", message = "dsm.hs 이메일이 아닙니다.")
    private String id;
    @NotBlank(message = "password에 값이 존재하지 않습니다.")
    @Size(max = 20, message = "길이가 20이 넘어서는 안됩니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 영문과 특수문자 숫자를 포함하며 8자 이상이어야 합니다")
    private String password;

    private Gender gender;

    @Size(max = 10, message = "별명은 10자 이내로 설정하여야한다.")
    private String nickName;

    private LocalDate birth;

    private School school;
    private int grade;
    private int classNum;

    private int number;

    @NotBlank(message = "name에 값이 존재하지 않습니다.")
    private String name;

    @NotBlank(message = "teacherId에 값이 존재하지 않습니다.")
    private String teacherId;

}
