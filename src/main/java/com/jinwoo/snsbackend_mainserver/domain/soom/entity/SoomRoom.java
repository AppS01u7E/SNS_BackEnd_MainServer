package com.jinwoo.snsbackend_mainserver.domain.soom.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.request.PostNoticeRequest;
import com.jinwoo.snsbackend_mainserver.global.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SoomRoom extends BaseEntity {

    @Id
    private String id;

    private String representativeId;

    @ElementCollection
    private List<String> memberIds = new ArrayList<>();

    private String title;

    private String info;

    private String teacherId;

    @ElementCollection
    private List<URL> profiles = new ArrayList<>();

    @OneToMany
    @JsonBackReference
    private List<Notice> notices = new ArrayList<>();

    private SoomType soomType;


    private String joinCode;

    public SoomRoom setJoinCode(String code){
        this.joinCode = code;
        return this;
    }


    public void addProfile(URL p){
        this.profiles.add(p);
    }

    public SoomRoom writeNoticeAndFile(PostNoticeRequest request, List<String> files){
        this.notices.add(new Notice().builder()
                        .title(request.getTitle())
                        .info(request.getInfo())
                        .fileKeys(files)
                        .build()
                );
        return this;
    }

    public SoomRoom writeNotice(PostNoticeRequest request){
        this.notices.add(new Notice().builder()
                .title(request.getTitle())
                .info(request.getInfo())
                .build()
        );
        return this;
    }

    public SoomRoom addMember(String memberId){
        this.memberIds.add(memberId);
        return this;
    }

    public Notice getLatestNotice(){
        return this.getNotices().get(getNotices().size()-1);
    }

}
