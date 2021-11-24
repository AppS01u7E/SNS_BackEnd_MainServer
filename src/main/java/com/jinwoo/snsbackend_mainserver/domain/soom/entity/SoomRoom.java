package com.jinwoo.snsbackend_mainserver.domain.soom.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheBlockInfo;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.request.PostNoticeRequest;
import com.jinwoo.snsbackend_mainserver.global.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SoomRoom {

    @Id
    private String id;

    private String representativeId;

    @ElementCollection
    private List<String> memberIds;

    private String title;

    private String info;

    private String teacherId;

    @ElementCollection
    private List<URL> profiles;

    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<Notice> notices;

    private SoomType soomType;

    @OneToMany(mappedBy = "id", cascade = CascadeType.REMOVE)
    private List<ScheBlockInfo> scheBlockInfoList;

    private String joinCode;

    @CreationTimestamp
    private LocalDate created;

    @UpdateTimestamp
    private LocalDateTime updated;


    public SoomRoom setJoinCode(String code){
        this.joinCode = code;
        return this;
    }


    public void addProfile(URL p){
        this.profiles.add(p);
    }


    public SoomRoom addMember(String memberId){
        this.memberIds.add(memberId);
        return this;
    }

    public Notice getLatestNotice(){
        return this.getNotices().get(this.getNotices().size()-1);
    }

    public SoomRoom setRoomType(SoomType type){
        this.soomType = type;
        return this;
    }

    public SoomRoom setTeacher(String teacherId){
        this.teacherId = teacherId;
        return this;
    }
}
