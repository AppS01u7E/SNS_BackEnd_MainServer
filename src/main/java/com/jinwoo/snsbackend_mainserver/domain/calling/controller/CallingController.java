package com.jinwoo.snsbackend_mainserver.domain.calling.controller;


import com.jinwoo.snsbackend_mainserver.domain.auth.service.AuthService;
import com.jinwoo.snsbackend_mainserver.domain.calling.payload.request.CallingClassAndGradeRequest;
import com.jinwoo.snsbackend_mainserver.domain.calling.payload.request.CallingClassRequest;
import com.jinwoo.snsbackend_mainserver.domain.calling.payload.request.CallingClubRequest;
import com.jinwoo.snsbackend_mainserver.domain.calling.payload.request.CallingMemberRequest;
import com.jinwoo.snsbackend_mainserver.domain.calling.service.CallingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calling")
public class CallingController {
    private final CallingService callingService;
    private final AuthService authService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void callingMember(@RequestBody CallingMemberRequest request){
        callingService.noticeToMember(request.getReceiverId(), request.getTitle(), request.getMessage());
    }


    @PostMapping("/class/grade")
    @ResponseStatus(HttpStatus.CREATED)
    public void noticeByClassAndGrade(@RequestBody CallingClassAndGradeRequest request){
        callingService.noticeByClassAndGrade(request.getGrade(), request.getClassNum(), request.getTitle(), request.getMessage());
    }


    @PostMapping("/club")
    @ResponseStatus(HttpStatus.CREATED)
    public void noticeByClub(@RequestBody CallingClubRequest request){
        callingService.noticeByClub(request.getSoomId(), request.getTitle(), request.getMessage());
    }

    @PostMapping("/grade")
    @ResponseStatus(HttpStatus.CREATED)
    public void noticeToClass(@RequestBody CallingClassRequest request){
        callingService.noticeToGrade(request.getGrade(), request.getTitle(), request.getMessage());
    }


}
