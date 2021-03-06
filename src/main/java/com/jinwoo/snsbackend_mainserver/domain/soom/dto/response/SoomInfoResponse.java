package com.jinwoo.snsbackend_mainserver.domain.soom.dto.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.Notice;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.security.DenyAll;
import javax.persistence.ElementCollection;
import javax.persistence.OneToMany;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SoomInfoResponse {

    private String id;

    private String representativeId;

    private List<String> memberIds;

    private String title;

    private String info;

    private String teacherId;

    private List<URL> profiles;

    private List<Notice> notices;

    private SoomType soomType;

    private String code;

    private LocalDate created;

    private LocalDateTime updated;
}
