package com.jinwoo.snsbackend_mainserver.domain.auth.controller;


import com.jinwoo.snsbackend_mainserver.domain.auth.exception.InvalidCodeException;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.LoginRequest;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.StudentSignupRequest;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.response.MemberResponse;
import com.jinwoo.snsbackend_mainserver.global.email.exception.EmailException;
import com.jinwoo.snsbackend_mainserver.global.email.payload.EmailSendRequest;
import com.jinwoo.snsbackend_mainserver.global.email.service.EmailService;
import com.jinwoo.snsbackend_mainserver.global.security.payload.TokenResponse;
import com.jinwoo.snsbackend_mainserver.domain.auth.service.AuthService;
import com.jinwoo.snsbackend_mainserver.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;
    private final RedisUtil redisUtils;


    @GetMapping("/email")
    public String getEmailAuthentication(@RequestBody EmailSendRequest email){
        String random = String.valueOf(UUID.randomUUID()).substring(0, 5).toUpperCase();
        try{
            redisUtils.setDataExpire(email.getEmail(), random, 300);
            return emailService.sendEmail(email.getEmail(), random);
        } catch (Exception e){
            throw new EmailException(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public TokenResponse signup(@RequestBody StudentSignupRequest signupRequest, @RequestParam String code){
        if (redisUtils.getData(signupRequest.getId()).equals(code))
            return authService.signup(signupRequest);
        throw new InvalidCodeException();
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }


    @GetMapping("/mypage")
    public MemberResponse getMypage(){
        return authService.mypage();
    }


    @GetMapping("/member/list")
    public List<MemberResponse> getMember(){
        return authService.getMember();
    }


    @GetMapping("/member")
    public MemberResponse getSepMember(@RequestParam String memberId){
        return authService.getSepMember(memberId);
    }


}
