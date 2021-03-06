package com.jinwoo.snsbackend_mainserver.domain.schedule.service;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.School;
import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.Memo;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request.*;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.ScheBlockInfo;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.SchoolMemoResponse;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.SchoolMonthScheduleResponse;

import java.io.IOException;
import java.util.List;

public interface ScheduleService {

    public Object getRangeSchedule(int grade, int classNum, int startDate, int endDate) throws IOException;
    public void editMemoInfo(Long memoId, String title, String info);
    public void deleteMemoInfo(Long infoId);

    public void writeMemoInfo(WriteMemoInfoRequest request);
    public void writeSoomMemoInfo(SoomMemoInfoRequest request);
    public void writePersonalMemoInfo(WritePersonalMemoInfoRequest request);

    public List<SchoolMemoResponse> getSchoolList(int year, int month, School school);

}
