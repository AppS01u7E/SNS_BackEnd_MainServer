package com.jinwoo.snsbackend_mainserver.domain.auth.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Member {
    @Id@NotNull
    private String id;
    @NotNull
    private String password;

    private Gender gender;

    private LocalDate birth;
    @Embedded
    private School school;

    private String name;
    @NotNull
    private Role role;

    private String phone;

    private String teacherId;

    private String info;
    @CreatedDate
    private LocalDate createAt;

}
