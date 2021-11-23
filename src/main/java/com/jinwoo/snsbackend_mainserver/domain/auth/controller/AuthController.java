package com.jinwoo.snsbackend_mainserver.domain.auth.controller;


import com.jinwoo.snsbackend_mainserver.domain.auth.exception.InvalidCodeException;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.*;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.response.MemberResponse;
import com.jinwoo.snsbackend_mainserver.global.email.service.EmailService;
import com.jinwoo.snsbackend_mainserver.global.security.payload.TokenResponse;
import com.jinwoo.snsbackend_mainserver.domain.auth.service.AuthService;
import com.jinwoo.snsbackend_mainserver.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;
    private final RedisUtil redisUtils;


    @PostMapping("/email/check")
    public void emailCheck(@RequestBody EmailCheckRequest request){
        authService.checkEmail(request.getEmail());
    }

    @GetMapping("/change/password")
    public void sendCode(){
        authService.sendCode();
    }

    @PutMapping("/change/password")
    public void checkChangePassword(@RequestBody CodeRequest request){
        redisUtils.getData(request.getCode());
    }

    @PostMapping("/change/password")
    public void changePassword(@RequestBody ChangePasswordRequest request){
        if (!redisUtils.getData(request.getCode()).equals(request.getCode())) throw new InvalidCodeException();
        authService.changePassword(request);
    }



    @PostMapping("/signup")
    public TokenResponse signup(@Valid @RequestBody StudentSignupRequest signupRequest){
        return authService.signup(signupRequest);
    }

    @PostMapping("/signup/teacher")
    public TokenResponse teacherSignup(@Valid TeacherSignupRequest request){
        return authService.teacherSignup(request);
    }



    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }


    @PostMapping("/reissue")
    public TokenResponse reissue(@RequestBody TokenResponse tokenResponse){
        return authService.reissue(tokenResponse);
    }

    @GetMapping("/mypage")
    public MemberResponse getMypage(){
        return authService.mypage();
    }

    @PatchMapping("/mypage")
    public MemberResponse editMypage(@RequestBody EditMypageRequest request){
        return authService.editMypage(request);
    }

    @GetMapping("/member/list")
    public List<MemberResponse> getMember(@RequestParam SearchQueryRequest searchQueryRequest, MemberSearchRequest memberSearchRequest){
        return authService.getMember(memberSearchRequest, searchQueryRequest);
    }


    @PostMapping("/member")
    public MemberResponse getSepMember(@RequestBody MemberIdRequest request){
        return authService.getSepMember(request.getMemberId());
    }




    @PostMapping("/chat/{soomRoomId}")
    public void ignoreSoomRoomChattingAlarm(@PathVariable String soomRoomId){
        authService.ignoreSoomChat(soomRoomId);
    }

    @PostMapping("/notice/{soomRoomId}")
    public void ignoreSoomRoomNoticeAlarm(@PathVariable String soomRoomId){
        authService.ignoreSoomNotice(soomRoomId);
    }

    @PostMapping("/notice/head/{soomRoomId}")
    public void headSoomRoomNoitceAlarm(@PathVariable String soomRoomId){
        authService.headSoomNotice(soomRoomId);
    }

    @PostMapping("/chat/head/{soomRoomId}")
    public void headSoomRoomChattingAlarm(@PathVariable String soomRoomId){
        authService.headSoomChat(soomRoomId);
    }




    @GetMapping("/code")
    public List<String> getGeneCode(@RequestParam int count){
        return authService.geneTeacherCode(count);
    }


}
