package com.jinwoo.snsbackend_mainserver.domain.calling.controller;


import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.response.MemberResponse;
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
    private final CurrentMember currentMember;


    @GetMapping("/member/all")
    public List<MemberResponse> getMember(){
        return callingService.getMember(currentMember.getMember().getSchool());
    }

    @GetMapping("/member/{memberId}")
    public MemberResponse getSepMember(@PathVariable String memberId){
        return callingService.getSepMember(currentMember.getMember().getSchool(), memberId);
    }

    @PostMapping("/{memberId}")
    public void callingMember(@PathVariable CallingMemberRequest request){
        callingService.callMember(request.getReceiverId(), request.getMessage());
    }

}
