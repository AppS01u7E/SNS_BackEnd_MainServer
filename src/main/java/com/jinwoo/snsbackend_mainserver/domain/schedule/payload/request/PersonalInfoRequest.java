package com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PersonalInfoRequest {

    private String title;
    private String info;
    private int yearMonth;
}
