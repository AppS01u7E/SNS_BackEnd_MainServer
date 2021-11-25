package com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class WritePersonalMemoInfoRequest {

    private LocalDate date;
    private int period;


    private String title;
    private String info;
}
