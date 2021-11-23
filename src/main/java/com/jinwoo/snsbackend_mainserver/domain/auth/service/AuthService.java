package com.jinwoo.snsbackend_mainserver.domain.auth.service;

import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.*;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.response.MemberResponse;
import com.jinwoo.snsbackend_mainserver.global.security.payload.TokenResponse;

import java.util.List;

public interface AuthService {


    public void checkEmail(String email);
    public void sendCode();
    public void changePassword(ChangePasswordRequest request);

    public TokenResponse reissue(TokenResponse tokenResponse);
    public TokenResponse signup(StudentSignupRequest signupRequest);
    public TokenResponse teacherSignup(TeacherSignupRequest request);

    public TokenResponse login(LoginRequest loginRequest);
    public List<MemberResponse> getMember(MemberSearchRequest memberSearchRequest, SearchQueryRequest request);
    public MemberResponse getSepMember(String memberId);

    public List<String> geneTeacherCode(int i);


    public void ignoreSoomChat(String soomRoomId);
    public void ignoreSoomNotice(String soomRoomId);
    public void headSoomChat(String soomRoomId);
    public void headSoomNotice(String soomRoomId);


    public MemberResponse mypage();
    public MemberResponse editMypage(EditMypageRequest request);


}
