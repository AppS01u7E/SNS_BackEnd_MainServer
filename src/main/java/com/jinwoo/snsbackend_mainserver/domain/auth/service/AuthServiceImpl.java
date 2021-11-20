package com.jinwoo.snsbackend_mainserver.domain.auth.service;

import com.jinwoo.snsbackend_mainserver.domain.auth.dao.MemberRepository;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Role;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.School;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.*;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.LoginRequest;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.StudentSignupRequest;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.response.MemberResponse;
import com.jinwoo.snsbackend_mainserver.global.email.service.EmailService;
import com.jinwoo.snsbackend_mainserver.global.security.payload.TokenResponse;
import com.jinwoo.snsbackend_mainserver.global.security.service.TokenProvider;
import com.jinwoo.snsbackend_mainserver.global.utils.CurrentMember;
import com.jinwoo.snsbackend_mainserver.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final CurrentMember currentMember;
    private final RedisUtil redisUtil;


    @Override
    public TokenResponse signup(StudentSignupRequest signupRequest) {

        if (memberRepository.findById(signupRequest.getId()).isPresent()) throw new MemberAlreadyExistsException();
        if (!signupRequest.getId().endsWith("dsm.hs.kr")) throw new NotSchoolEmailException();
        Member member = memberRepository.save(
                Member.builder()
                .id(signupRequest.getId())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .gender(signupRequest.getGender())
                .birth(signupRequest.getBirth())
                .school(School.DAEDOK)
                .name(signupRequest.getName())
                .role(Role.ROLE_STUDENT)
                .build()
        );
        log.info(member.getId() + "  회원가입 성공");
        return tokenProvider.createToken(member.getId(), member.getRole());
    }



    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findById(loginRequest.getId()).orElseThrow(MemberNotFoundException::new);
        if (passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())){
            redisUtil.setData(loginRequest.getId() + "devT", loginRequest.getDeviceToken());
            return tokenProvider.createToken(member.getId(), member.getRole());
        }

        throw new IncorrectPasswordException();
    }

    @Override
    public MemberResponse mypage() {
        Member member = currentMember.getMember();
        return MemberResponse.builder()
                .gender(member.getGender())
                .birth(member.getBirth())
                .name(member.getName())
                .id(member.getId())
                .alarmSetting(member.getAlarmSetting())
                .build();
    }


    @Override
    public List<MemberResponse> getMember(){

        return memberRepository.findAllBySchool(currentMember.getMember().getSchool()).stream().map(
                member -> MemberResponse.builder()
                        .id(member.getId())
                        .name(member.getName())
                        .birth(member.getBirth())
                        .gender(member.getGender())
                        .build()
        ).collect(Collectors.toList());
    }


    @Override
    public MemberResponse getSepMember(String memberId) {
        Member member = memberRepository.findBySchoolAndId(currentMember.getMember().getSchool()
                , memberId).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .birth(member.getBirth())
                .gender(member.getGender())
                .build();
    }

}
