package com.jinwoo.snsbackend_mainserver.domain.auth.service;

import com.jinwoo.snsbackend_mainserver.domain.auth.dao.MemberRepository;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Role;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.School;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.IncorrectPasswordException;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.MemberAlreadyExistsException;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.MemberNotFoundException;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.LoginRequest;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.StudentSignupRequest;
import com.jinwoo.snsbackend_mainserver.global.email.service.EmailService;
import com.jinwoo.snsbackend_mainserver.global.security.payload.TokenResponse;
import com.jinwoo.snsbackend_mainserver.global.security.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final EmailService emailService;


    @Override
    public TokenResponse signup(StudentSignupRequest signupRequest) {

        if (memberRepository.findById(signupRequest.getId()).isPresent()) throw new MemberAlreadyExistsException();
        Member member = memberRepository.save(
                Member.builder()
                .id(signupRequest.getId())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .gender(signupRequest.getGender())
                .birth(signupRequest.getBirth())
                .school(new School(signupRequest.getSchoolName(), signupRequest.getAreaCode(), signupRequest.getScoolCode(),
                        signupRequest.getGrade(), signupRequest.getClassNum()))
                .name(signupRequest.getName())
                .role(Role.ROLE_STUDENT)
                .email(signupRequest.getEmail())
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
