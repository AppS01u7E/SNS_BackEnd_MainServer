package com.jinwoo.snsbackend_mainserver.domain.auth.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import com.jinwoo.snsbackend_mainserver.global.utils.BaseEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Id
    @NotNull
    @Column(name = "id")
    private String id;
    @NotNull
    private String password;

    private Gender gender;

    private LocalDate birth;

    private School school;

    private int grade;

    private int classNum;

    @ManyToMany
    @JsonBackReference
    @JoinColumn(name = "soomroom_id")
    private List<SoomRoom> soomRooms;

    private String name;

    @NotNull
    private Role role;

    private String info;

    private AlarmSetting alarmSetting;


}
