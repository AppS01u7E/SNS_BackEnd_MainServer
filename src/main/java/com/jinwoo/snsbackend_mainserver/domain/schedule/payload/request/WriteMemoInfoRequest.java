package com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request;


import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheduleType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WriteScheBlockInfoRequest {
    private int grade;
    private int classNum;
    private LocalDate date;
    private int period;

    @Length(max = 15)
    private String title;
    private String info;

    private String subjectName;

    private ScheduleType type;
}
