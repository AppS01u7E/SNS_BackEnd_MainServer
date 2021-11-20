package com.jinwoo.snsbackend_mainserver.domain.calling.service;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.School;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.response.MemberResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CallingService {

    public void noticeByClassAndGrade(int grade, int classNum, String title, String message);
    public void noticeByClub(String clubId, String title, String message);
    public void noticeToMember(String receiverId, String title, String message);
    public void noticeToClass(int grade, String title, String message);

}
