package com.jinwoo.snsbackend_mainserver.domain.schedule.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.LocalScheReturnResponseDayDto;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import com.jinwoo.snsbackend_mainserver.global.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ScheBlockInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String writer;

    private int grade;
    private int classNum;
    private LocalDate date;
    private int period;
    private String name;

    @Column(length = 15)
    private String title;
    private String info;

    private ScheduleType dayScheType;

    private int YMonth;

    private SoomRoom soomRoom;


    public ScheBlockInfo(String name, int period, int grade, int classNum, LocalDate date, int yearMonth) {
        this.name = name;
        this.period = period;
        this.grade = grade;
        this.classNum = classNum;
        this.date = date;
        this.YMonth = yearMonth;
    }

    public ScheBlockInfo editTitle(String title){
        this.title = title;
        return this;
    }

    public ScheBlockInfo editInfo(String info){
        this.info = info;
        return this;
    }

    public void delete(){
        this.delete();
    }

    public LocalScheReturnResponseDayDto.Subject toSubject(){
        return new LocalScheReturnResponseDayDto.Subject(this.getName(), this.getPeriod(),
                this.getGrade(), this.getClassNum(), this.getDate(), this.getTitle(), this.getInfo(), this.getDayScheType());
    }

}
