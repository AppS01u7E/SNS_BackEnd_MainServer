package com.jinwoo.snsbackend_mainserver.domain.calling.service;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.School;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.response.MemberResponse;

import java.util.List;

public interface CallingService {
    public List<MemberResponse> getMember(School school);
    public MemberResponse getSepMember(School school, String memberId);
    public void callMember(String receiverId, String message);

}
