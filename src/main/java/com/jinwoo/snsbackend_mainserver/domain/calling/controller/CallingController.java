package com.jinwoo.snsbackend_mainserver.domain.calling.controller;


import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.response.MemberResponse;
import com.jinwoo.snsbackend_mainserver.domain.calling.payload.request.CallingClassAndGradeRequest;
import com.jinwoo.snsbackend_mainserver.domain.calling.payload.request.CallingClassRequest;
import com.jinwoo.snsbackend_mainserver.domain.calling.payload.request.CallingClubRequest;
import com.jinwoo.snsbackend_mainserver.domain.calling.payload.request.CallingMemberRequest;
import com.jinwoo.snsbackend_mainserver.domain.calling.service.CallingService;
import com.jinwoo.snsbackend_mainserver.global.utils.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calling")
public class CallingController {
    private final CallingService callingService;

    @PostMapping("/{memberId}")
    public void callingMember(@PathVariable CallingMemberRequest request){
        callingService.noticeToMember(request.getReceiverId(), request.getTitle(), request.getMessage());
    }


    @PostMapping("/class/grade")
    public void noticeByClassAndGrade(@RequestBody CallingClassAndGradeRequest request){
        callingService.noticeByClassAndGrade(request.getGrade(), request.getClassNum(), request.getTitle(), request.getMessage());
    }

    @PostMapping("/club")
    public void noticeByClub(@RequestBody CallingClubRequest request){
        callingService.noticeByClub(request.getSoomId(), request.getTitle(), request.getMessage());
    }

    @PostMapping("/class")
    public void noticeToClass(@RequestBody CallingClassRequest request){
        callingService.noticeToClass(request.getGrade(), request.getTitle(), request.getMessage());
    }

    
}
