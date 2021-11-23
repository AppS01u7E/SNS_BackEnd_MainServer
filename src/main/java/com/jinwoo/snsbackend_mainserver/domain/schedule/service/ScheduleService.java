package com.jinwoo.snsbackend_mainserver.domain.schedule.service;

import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheBlockInfo;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request.IdentifyScheInfoBlockRequest;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request.PersonalInfoRequest;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request.WriteScheBlockInfoRequest;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.LocalScheReturnResponseDayDto;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.SchoolMonthScheduleResponse;

import java.io.IOException;
import java.util.List;

public interface ScheduleService {

    public Object getRangeSchedule(int grade, int classNum, int startDate, int endDate) throws IOException;
    public WriteScheBlockInfoRequest editInfo(WriteScheBlockInfoRequest request);
    public void deleteInfo(IdentifyScheInfoBlockRequest request);
    public WriteScheBlockInfoRequest writeInfo(WriteScheBlockInfoRequest request);
    public ScheBlockInfo getSepSchduleInfo(int grade, int classNum, int period, int sepDate);
    public List<SchoolMonthScheduleResponse> getSchoolList(int yearMonth);

    public List<ScheBlockInfo> getPersonalList(int yeaerMonth);
}
