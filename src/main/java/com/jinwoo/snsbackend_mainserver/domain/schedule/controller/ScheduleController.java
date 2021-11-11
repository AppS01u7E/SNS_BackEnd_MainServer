package com.jinwoo.snsbackend_mainserver.domain.schedule.controller;

import com.jinwoo.snsbackend_mainserver.domain.auth.exception.MemberAlreadyExistsException;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.LocalScheReturnResponseDayDto;
import com.jinwoo.snsbackend_mainserver.domain.schedule.service.ScheduleService;
import com.jinwoo.snsbackend_mainserver.domain.schedule.service.ScheduleServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neiseApi.School;
import neiseApi.payload.schoolInfo.SchoolShorten;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
    private final ScheduleService scheduleService;


    @GetMapping("/{grade}/{classNum}")
    public List<LocalScheReturnResponseDayDto> getSchedule(@RequestParam String schoolCode, @RequestParam int startDate, @RequestParam int endDate,
                                                           @PathVariable int grade, @PathVariable int classNum) throws IOException {

        return scheduleService.getSchedule(grade, classNum, startDate, endDate, schoolCode);
    }

}
