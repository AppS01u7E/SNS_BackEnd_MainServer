package com.jinwoo.snsbackend_mainserver.domain.soom.entity;


import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ownerId;

    @ElementCollection
    private List<String> memberIds = new ArrayList<>();

    private String info;


    private String teacherId;



}
