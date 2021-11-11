package com.jinwoo.snsbackend_mainserver.domain.auth.controller;


import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.LoginRequest;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.SignupRequest;
import com.jinwoo.snsbackend_mainserver.global.email.exception.EmailException;
import com.jinwoo.snsbackend_mainserver.global.email.payload.EmailSendRequest;
import com.jinwoo.snsbackend_mainserver.global.email.service.EmailService;
import com.jinwoo.snsbackend_mainserver.global.security.payload.TokenResponse;
import com.jinwoo.snsbackend_mainserver.domain.auth.service.AuthService;
import com.jinwoo.snsbackend_mainserver.global.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;
    private final RedisUtils redisUtils;

//    @PostMapping("/phone")
//    public String phoneCert(@RequestBody String phoneNum){
//        return "문자 전송 활성화시 발생되는 요금으로 인해 개발 과정 중 메시지 전송은 작동하지 않습니다. \n" +
//                "언제든 SMS를 활성화 할 순 있으나, 개발 기간 중 활성화는 특별한 사안이 아닐시 작동하지 않습니다.\n" +
//                "프론트 개발시 이 url에 요청시 HttpStatusCode 200 이 뜨는지만 확인한 후, 회원가입은 포스트맨으로 진행해주세요.\n" +
//                "인증번호: " + authService.sendSms(phoneNum);
//    }

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
    public TokenResponse signup(@RequestBody SignupRequest signupRequest){
        return authService.signup(signupRequest);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

}
