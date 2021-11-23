package com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.School;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class SchoolScheduleRequest {
    private School school;
    private int yearMonth;


}
