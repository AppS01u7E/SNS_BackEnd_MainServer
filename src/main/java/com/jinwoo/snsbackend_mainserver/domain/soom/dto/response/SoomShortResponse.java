package com.jinwoo.snsbackend_mainserver.domain.soom.dto.response;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.Notice;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomType;
import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.OneToMany;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SoomShortResponse {
    private String id;

    private String representativeId;

    private int memberCounts;

    private String title;

    private String info;

    private String teacherId;

    private List<URL> profiles;

    private SoomType soomType;
}
