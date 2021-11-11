package com.jinwoo.snsbackend_mainserver.domain.schedule.service;

import com.jinwoo.snsbackend_mainserver.domain.schedule.exception.ScheduleException;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.LocalScheReturnResponseDayDto;
import com.jinwoo.snsbackend_mainserver.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neiseApi.School;

import neiseApi.payload.schoolInfo.SchoolShorten;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    @Value("${neis.serviceKey}")
    private String serviceKey;


    @Override
    public List<LocalScheReturnResponseDayDto> getSchedule(int grade, int classNum, int startDate, int endDate, String schoolCode) throws IOException {
        School school = new School(serviceKey);
        try {
            return school.getSchoolSchedule(schoolCode, 1, 3, 20211111, 20211111)
                    .stream().map(
                            scheReturnResponseDayDto -> new LocalScheReturnResponseDayDto(scheReturnResponseDayDto.getGrade(),
                                    scheReturnResponseDayDto.getClassNum(), scheReturnResponseDayDto.getTotalCount(), scheReturnResponseDayDto.getDay(), scheReturnResponseDayDto.getSubjects().stream()
                                    .map(
                                            subject -> new LocalScheReturnResponseDayDto.Subject(subject.getName(), subject.getPeriod())
                                    ).collect(Collectors.toList()))).collect(Collectors.toList());
        } catch (Exception e){
            throw new ScheduleException(e.getMessage());
        }
    }



}
