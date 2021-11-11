package com.jinwoo.snsbackend_mainserver.domain.schedule.service;

import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.LocalScheReturnResponseDayDto;
import org.apache.maven.lifecycle.Schedule;

import java.io.IOException;
import java.util.List;

public interface ScheduleService {

    public List<LocalScheReturnResponseDayDto> getSchedule(int grade, int classNum, int startDate, int endDate, String schoolCode) throws IOException;
}
