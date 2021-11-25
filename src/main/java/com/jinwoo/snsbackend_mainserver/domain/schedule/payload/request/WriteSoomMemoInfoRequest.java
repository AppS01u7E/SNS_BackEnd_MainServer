package com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request;


import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheduleType;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WriteSoomMemoInfoRequest {


    private LocalDate date;
    private int grade;
    private int classNum;
    private int period;



    private String title;
    private String info;
    private String soomRoomId;

    private ScheduleType type;

}
