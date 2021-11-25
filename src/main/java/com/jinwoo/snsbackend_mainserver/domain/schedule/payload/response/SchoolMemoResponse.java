package com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response;


import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheduleType;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class SchoolMemoResponse {

    private Long id;
    private String title;
    private String info;
    private ScheduleType scheduleType;
    private String writerId;
    private LocalDate date;
    private SoomRoom soomRoom;

}
