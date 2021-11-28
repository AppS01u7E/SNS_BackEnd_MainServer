package com.jinwoo.snsbackend_mainserver.domain.auth.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.EditMypageRequest;
import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.Memo;
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

    private String nickName;

    private Gender gender;

    private LocalDate birth;

    private School school;

    private int grade;

    private int classNum;

    private int number;

    @ManyToMany
    @JsonManagedReference
    @JoinColumn(name = "soomroom_id")
    private List<SoomRoom> soomRooms;

    private String name;

    @NotNull
    private Role role;

    private String info;

    @OneToMany(mappedBy = "id", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Memo> memoList;

    @ElementCollection
    private List<String> noticeIgnoreList;

    @ElementCollection
    private List<String> chatIgnoreList;

    private boolean isLocked;

    @ElementCollection
    private List<String> badges;



    public Member addNoticeIgnorList(String soomRoomId){
        this.noticeIgnoreList.add(soomRoomId);
        return this;
    }

    public Member removeObjectFromNoticeIgnoreList(String soomRoomId){
        this.noticeIgnoreList.remove(soomRoomId);
        return this;
    }

    public Member addChatIgnoreList(String soomRoomId){
        this.chatIgnoreList.add(soomRoomId);
        return this;
    }

    public Member removeObjectFromChatIgnoreList(String soomRoomId){
        this.chatIgnoreList.remove(soomRoomId);
        return this;
    }

    public Member editMypage(EditMypageRequest request){
        this.gender = request.getGender();
        this.birth = request.getBirth();
        this.school = request.getSchool();
        this.grade = request.getGrade();
        this.classNum = request.getClassNum();
        this.info = request.getInfo();

        return this;
    }

    public Member joinSoom(SoomRoom soomRoom){
        this.soomRooms.add(soomRoom);
        return this;
    }

    public Member changePassword(String password){
        this.password = password;
        return this;
    }

    public Member addBadge(String badge){
        this.badges.add(badge);
        return this;
    }

    public Member preSoomDelete(SoomRoom soomRoom){
        this.soomRooms.remove(soomRoom);
        return this;
    }

}
