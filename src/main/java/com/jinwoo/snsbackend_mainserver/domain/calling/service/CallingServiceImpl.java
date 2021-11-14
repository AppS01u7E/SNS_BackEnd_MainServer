package com.jinwoo.snsbackend_mainserver.domain.calling.service;


import com.jinwoo.snsbackend_mainserver.domain.auth.dao.MemberRepository;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Role;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.School;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.MemberNotFoundException;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.response.MemberResponse;
import com.jinwoo.snsbackend_mainserver.domain.calling.exception.CallingException;
import com.jinwoo.snsbackend_mainserver.global.exception.LowAuthenticationException;
import com.jinwoo.snsbackend_mainserver.global.exception.DataCannotBringException;
import com.jinwoo.snsbackend_mainserver.global.firebase.service.FcmServiceImpl;
import com.jinwoo.snsbackend_mainserver.global.utils.CurrentMember;
import com.jinwoo.snsbackend_mainserver.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CallingServiceImpl implements CallingService {
    private final FcmServiceImpl fcmService;
    private final RedisUtil redisUtil;
    private final CurrentMember currentMember;
    private final MemberRepository memberRepository;


    @Override
    public List<MemberResponse> getMember(School school){
        return memberRepository.findAllBySchool(school).stream().map(
                member -> MemberResponse.builder()
                        .id(member.getId())
                        .name(member.getName())
                        .birth(member.getBirth())
                        .gender(member.getGender())
                        .teacherId(member.getTeacherId())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public MemberResponse getSepMember(School school, String memberId) {
        Member member = memberRepository.findBySchoolAndId(school, memberId).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .birth(member.getBirth())
                .teacherId(member.getTeacherId())
                .gender(member.getGender())
                .build();
    }


    @Override
    public void callMember(String receiverId, String message){
        Member member = currentMember.getMember();
        if (member.getRole().equals(Role.ROLE_STUDENT)){
            throw new LowAuthenticationException();
        }
        try {
            fcmService.sendMessageTo(redisUtil.getData(receiverId + "devT"), member.getId() + "호출하셨습니다", message);
        } catch (IOException e){
            throw new DataCannotBringException();
        } catch (Exception e){
            throw new CallingException(e.getMessage());
        }
    }


}
