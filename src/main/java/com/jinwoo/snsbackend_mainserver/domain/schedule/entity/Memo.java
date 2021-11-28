package com.jinwoo.snsbackend_mainserver.domain.schedule.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.LocalScheReturnResponseDayDto;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String info;

    private ScheduleType scheduleType;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JsonBackReference
    private Member writer;

    private int grade;

    private int classNum;

    private int period;

    private LocalDate date;

    @OneToOne
    @Nullable
    private SoomRoom soomRoom;


    public void setTitle(String title){
        this.title = title;
    }

    public void setInfo(String info){
        this.info = info;
    }

    public Memo preDelete(){
        this.writer = null;
        this.soomRoom = null;
        return this;
    }


}
