package com.jinwoo.snsbackend_mainserver.domain.auth.controller;


import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.LoginRequest;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.StudentSignupRequest;
import com.jinwoo.snsbackend_mainserver.global.email.exception.EmailException;
import com.jinwoo.snsbackend_mainserver.global.email.payload.EmailSendRequest;
import com.jinwoo.snsbackend_mainserver.global.email.service.EmailService;
import com.jinwoo.snsbackend_mainserver.global.security.payload.TokenResponse;
import com.jinwoo.snsbackend_mainserver.domain.auth.service.AuthService;
import com.jinwoo.snsbackend_mainserver.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public TokenResponse signup(@RequestBody StudentSignupRequest signupRequest){
        return authService.signup(signupRequest);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

}
