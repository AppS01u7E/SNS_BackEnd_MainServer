package com.jinwoo.snsbackend_mainserver.domain.auth.payload.response;


import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Gender;
import lombok.*;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {

    private String id;

    private Gender gender;

    private LocalDate birth;

    private String name;

    private String teacherId;

}
