package com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request;

import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheduleType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class SoomMemoInfoRequest {

    private LocalDate date;


    private int period;

    private String title;
    private String info;
    private String soomRoomId;
}
