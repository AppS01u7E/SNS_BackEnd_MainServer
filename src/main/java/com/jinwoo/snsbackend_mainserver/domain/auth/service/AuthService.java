package com.jinwoo.snsbackend_mainserver.domain.auth.service;

import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.LoginRequest;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.StudentSignupRequest;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.response.MemberResponse;
import com.jinwoo.snsbackend_mainserver.global.security.payload.TokenResponse;

import java.util.List;

public interface AuthService {

    public TokenResponse signup(StudentSignupRequest signupRequest);
    public TokenResponse login(LoginRequest loginRequest);
    public MemberResponse mypage();
    public List<MemberResponse> getMember();
    public MemberResponse getSepMember(String memberId);
}
