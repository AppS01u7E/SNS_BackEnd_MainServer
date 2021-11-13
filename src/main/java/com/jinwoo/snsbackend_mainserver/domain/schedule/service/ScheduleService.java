package com.jinwoo.snsbackend_mainserver.domain.schedule.service;

import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheBlockInfo;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request.IdentifyScheInfoBlockRequest;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request.WriteScheBlockInfoRequest;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.LocalScheReturnResponseDayDto;

import java.io.IOException;

public interface ScheduleService {

    public Object getRangeSchedule(int grade, int classNum, int startDate, int endDate, String schoolCode) throws IOException;
    public WriteScheBlockInfoRequest editInfo(WriteScheBlockInfoRequest request);
    public void deleteInfo(IdentifyScheInfoBlockRequest request);
    public WriteScheBlockInfoRequest writeInfo(WriteScheBlockInfoRequest request);
    public ScheBlockInfo getSepSchduleInfo(int grade, int classNum, String schoolCode, int period, int sepDate);
}
