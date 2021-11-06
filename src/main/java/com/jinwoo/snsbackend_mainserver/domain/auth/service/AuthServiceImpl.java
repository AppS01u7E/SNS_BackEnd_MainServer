package com.jinwoo.snsbackend_mainserver.domain.auth.service;

import com.jinwoo.snsbackend_mainserver.domain.auth.dao.MemberRepository;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Role;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.IncorrectPasswordException;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.MemberAlreadyExistsException;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.MemberNotFoundException;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.LoginRequest;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.SignupRequest;
import com.jinwoo.snsbackend_mainserver.global.security.payload.TokenResponse;
import com.jinwoo.snsbackend_mainserver.global.security.service.TokenProvider;
import com.jinwoo.snsbackend_mainserver.global.sms.service.ShortMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final ShortMessageService shortMessageService;

    @Override
    public String sendSms(String phoneNum) {
        String random = UUID.randomUUID().toString().toUpperCase().substring(0, 3);
        //shortMessageService.sendSMS(phoneNum, random); <- 아직 사용하지 않음
        return random;
    }

    @Override
    public TokenResponse signup(SignupRequest signupRequest) {

        if (memberRepository.findById(signupRequest.getId()).isPresent()) throw new MemberAlreadyExistsException();
        Member member = memberRepository.save(
                Member.builder()
                .id(signupRequest.getId())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .gender(signupRequest.getGender())
                .birth(signupRequest.getBirth())
                .school(signupRequest.getSchool())
                .name(signupRequest.getName())
                .role(Role.ROLE_STUDENT)
                .phone(signupRequest.getPhone())
                .teacherId(signupRequest.getTeacherId())
                .build()
        );
        log.info(member.getId() + "  회원가입 성공");
        return tokenProvider.createToken(member.getId(), member.getRole());
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findById(loginRequest.getId()).orElseThrow(MemberNotFoundException::new);
        if (passwordEncoder.matches(loginRequest.getPassword(), member.getPassword()))
            return tokenProvider.createToken(member.getId(), member.getRole());
        throw new IncorrectPasswordException();
    }
}
