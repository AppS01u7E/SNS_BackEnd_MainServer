package com.jinwoo.snsbackend_mainserver.domain.calling.service;


import com.jinwoo.snsbackend_mainserver.domain.auth.dao.MemberRepository;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.School;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.MemberNotFoundException;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.response.MemberResponse;
import com.jinwoo.snsbackend_mainserver.domain.calling.exception.CallingException;
import com.jinwoo.snsbackend_mainserver.domain.soom.dao.SoomRepository;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.SoomShortResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import com.jinwoo.snsbackend_mainserver.domain.soom.exception.SoomNotFoundException;
import com.jinwoo.snsbackend_mainserver.global.exception.DataCannotBringException;
import com.jinwoo.snsbackend_mainserver.global.firebase.service.FcmServiceImpl;
import com.jinwoo.snsbackend_mainserver.global.utils.CurrentMember;
import com.jinwoo.snsbackend_mainserver.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CallingServiceImpl implements CallingService {
    private final FcmServiceImpl fcmService;
    private final RedisUtil redisUtil;
    private final CurrentMember currentMember;
    private final MemberRepository memberRepository;
    private final SoomRepository soomRepository;


    @Override
    public void noticeByClassAndGrade(int grade, int classNum, String title, String message){
        List<Member> receivers = memberRepository.findAllBySchoolAndGradeAndClassNum(currentMember.getMember().getSchool(), grade, classNum);
        try {
            for (Member receiver : receivers) {
                log.info(fcmService.sendMessageTo(redisUtil.getData(receiver.getId() + "devT"), title, message));
            }
        } catch (IOException e){
            throw new DataCannotBringException();
        }
    }


    @Override
    public void noticeByClub(String clubId, String title, String message){
        List<Member> members = memberRepository.findAllBySchoolAndSoomRoomsContaining(currentMember.getMember().getSchool(),
                soomRepository.findById(clubId).orElseThrow(SoomNotFoundException::new));
        try{
            for (Member receiver: members){
                log.info(fcmService.sendMessageTo(redisUtil.getData(receiver.getId() + "devT"), title, message));
            }
        } catch (IOException e){
            throw new DataCannotBringException();
        }
    }

    @Override
    public void noticeToMember(String receiverId, String title, String message){
        try {

            log.info(fcmService.sendMessageTo(redisUtil.getData(receiverId + "devT"), title, message));
        } catch (IOException e){
            throw new DataCannotBringException();
        }
    }

    @Override
    public void noticeToClass(int grade, String title, String message) {
        List<Member> members = memberRepository.findAllBySchoolAndGrade(School.DAEDOK, grade);
        try{
            for (Member receiver: members){
                log.info(fcmService.sendMessageTo(redisUtil.getData(receiver.getId() + "devT"), title, message));
            }
        } catch (IOException e){
            throw new DataCannotBringException();
        }
    }

}
