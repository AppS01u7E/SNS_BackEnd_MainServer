package com.jinwoo.snsbackend_mainserver.domain.auth.entity;

import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @NotNull
    @Column(name = "id")
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
