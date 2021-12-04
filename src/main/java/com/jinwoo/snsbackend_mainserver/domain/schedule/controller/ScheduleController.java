package com.jinwoo.snsbackend_mainserver.domain.schedule.controller;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.School;
import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.Memo;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.ScheBlockInfo;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request.*;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.LocalScheReturnResponseDayDto;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.SchoolMemoResponse;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.SchoolMonthScheduleResponse;
import com.jinwoo.snsbackend_mainserver.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
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



    @GetMapping("/{grade}/{classNum}")
    public List<LocalScheReturnResponseDayDto> getRangeSchedule(@RequestParam int startDate, @RequestParam int endDate,
                                                           @PathVariable int grade, @PathVariable int classNum) throws IOException {
        return (List<LocalScheReturnResponseDayDto>) scheduleService.getRangeSchedule(grade, classNum, startDate, endDate);
    }


    @GetMapping("/school")
    @ResponseStatus(HttpStatus.OK)
    public List<SchoolMemoResponse> getSchoolSchedule(@RequestParam int year, @RequestParam int month, @RequestParam School school) {
        return scheduleService.getSchoolList(year, month, school);
    }


    @PostMapping("/teacher")
    @ResponseStatus(HttpStatus.CREATED)
    public void writeSchoolInfo(@RequestBody WriteMemoInfoRequest request){
        scheduleService.writeMemoInfo(request);
    }

    @PostMapping("/soom")
    @ResponseStatus(HttpStatus.CREATED)
    public void wrtieSoomInfo(@RequestBody SoomMemoInfoRequest request){
        scheduleService.writeSoomMemoInfo(request);
    }

    @PostMapping("/personal")
    @ResponseStatus(HttpStatus.CREATED)
    public void writePersonalInfo(@RequestBody WritePersonalMemoInfoRequest request){
        scheduleService.writePersonalMemoInfo(request);
    }



    @PatchMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void editSchoolSchedule(@RequestBody EditScheduleRequest request){
        scheduleService.editMemoInfo(request.getMemoId(), request.getTitle(), request.getInfo());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInfo(@RequestParam Long infoId){
        scheduleService.deleteMemoInfo(infoId);
    }



}
