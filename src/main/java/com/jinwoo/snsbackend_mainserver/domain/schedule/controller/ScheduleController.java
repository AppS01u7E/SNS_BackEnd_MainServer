package com.jinwoo.snsbackend_mainserver.domain.schedule.controller;

import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheBlockInfo;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request.*;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.LocalScheReturnResponseDayDto;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.SchoolMonthScheduleResponse;
import com.jinwoo.snsbackend_mainserver.domain.schedule.service.ScheduleService;
import com.jinwoo.snsbackend_mainserver.domain.schedule.service.ScheduleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;



    @GetMapping("/sep/{grade}/{classNum}")
    public ResponseEntity<?> getSepRangeSchedule(@PathVariable int grade, @PathVariable int classNum,
                                              @RequestParam int period, @RequestParam int sepDate){
        return ResponseEntity.ok().body(scheduleService.getSepSchduleInfo(grade, classNum, period, sepDate));
    }


    @GetMapping("/{grade}/{classNum}")
    public List<LocalScheReturnResponseDayDto> getRangeSchedule(@RequestParam int startDate, @RequestParam int endDate,
                                                           @PathVariable int grade, @PathVariable int classNum) throws IOException {
        return (List<LocalScheReturnResponseDayDto>) scheduleService.getRangeSchedule(grade, classNum, startDate, endDate);
    }


    @PostMapping("/school")
    @ResponseStatus(HttpStatus.CREATED)
    public List<SchoolMonthScheduleResponse> getSchoolSchedule(@RequestBody SchoolScheduleRequest school) {
        return scheduleService.getSchoolList(school.getYearMonth());
    }


    @PatchMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WriteScheBlockInfoRequest editSchool(@RequestBody WriteScheBlockInfoRequest request){
        return scheduleService.editInfo(request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInfo(@RequestBody IdentifyScheInfoBlockRequest request){
        scheduleService.deleteInfo(request);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void writeInfo(@RequestBody WriteScheBlockInfoRequest request){
        scheduleService.writeInfo(request);
    }


    @PostMapping("/personal")
    public List<ScheBlockInfo> getPersonalScheduleList(@RequestBody PersonalInfoRequest request){
        return scheduleService.getPersonalList(request.getYearMonth());
    }






}
