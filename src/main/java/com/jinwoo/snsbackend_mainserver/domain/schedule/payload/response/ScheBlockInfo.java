package com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.Memo;
import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheduleType;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.LocalScheReturnResponseDayDto;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import com.jinwoo.snsbackend_mainserver.global.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ScheBlockInfo extends BaseEntity {

    private String writer;

    private int grade;
    private int classNum;
    private LocalDate date;
    private int period;
    private String name;

    private List<Memo> memoList;

    private ScheduleType dayScheType;

    private int yMonth;

}
