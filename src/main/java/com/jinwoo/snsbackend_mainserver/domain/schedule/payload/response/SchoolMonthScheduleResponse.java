package com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response;


import com.amazonaws.services.cloudformation.model.PhysicalResourceIdContextKeyValuePair;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolMonthScheduleResponse {

    private String event;

    private LocalDate day;

    public SchoolMonthScheduleResponse addEvent(String eventName){
        this.event = eventName;
        return this;
    }


}
