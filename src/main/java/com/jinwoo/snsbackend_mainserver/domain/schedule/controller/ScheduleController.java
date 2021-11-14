package com.jinwoo.snsbackend_mainserver.domain.schedule.controller;

import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheBlockInfo;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request.IdentifyScheInfoBlockRequest;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request.WriteScheBlockInfoRequest;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.LocalScheReturnResponseDayDto;
import com.jinwoo.snsbackend_mainserver.domain.schedule.service.ScheduleService;
import com.jinwoo.snsbackend_mainserver.global.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
    private final ScheduleService scheduleService;




    @GetMapping("/sep/{grade}/{classNum}")
    public ScheBlockInfo getSepRangeSchedule(@PathVariable int grade, @PathVariable int classNum, @RequestParam String schoolCode,
                                          @RequestParam int period, @RequestParam int sepDate) throws IOException {
        return scheduleService.getSepSchduleInfo(grade, classNum, schoolCode, period, sepDate);
    }


    @GetMapping("/{grade}/{classNum}")
    public List<LocalScheReturnResponseDayDto> getRangeSchedule(@RequestParam String schoolCode, @RequestParam int startDate, @RequestParam int endDate,
                                                           @PathVariable int grade, @PathVariable int classNum) throws IOException {
        return (List<LocalScheReturnResponseDayDto>) scheduleService.getRangeSchedule(grade, classNum, startDate, endDate, schoolCode);
    }


    @PostMapping
    public WriteScheBlockInfoRequest writeInfoScheduleBlock(@RequestBody WriteScheBlockInfoRequest request){
        return scheduleService.writeInfo(request);
    }

    @PatchMapping
    public WriteScheBlockInfoRequest editInfoScheduleBlock(@RequestBody WriteScheBlockInfoRequest request){
        return scheduleService.editInfo(request);
    }

    @DeleteMapping
    public String deleteScheduleBlock(@RequestBody IdentifyScheInfoBlockRequest request){
        scheduleService.deleteInfo(request);
        return "Success";
    }





}
