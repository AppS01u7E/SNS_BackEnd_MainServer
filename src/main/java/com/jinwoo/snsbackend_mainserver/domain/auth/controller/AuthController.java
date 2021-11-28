package com.jinwoo.snsbackend_mainserver.domain.auth.controller;


import com.jinwoo.snsbackend_mainserver.domain.auth.dao.MemberRepository;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Role;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.InvalidCodeException;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.*;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.response.MemberResponse;
import com.jinwoo.snsbackend_mainserver.global.security.payload.TokenResponse;
import com.jinwoo.snsbackend_mainserver.domain.auth.service.AuthService;
import com.jinwoo.snsbackend_mainserver.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtils;

    private final PasswordEncoder passwordEncoder;


    @PostMapping("/email/check")
    public void emailCheck(@RequestParam String email){
        authService.checkEmail(email);
    }




    @GetMapping("/password")
    public void sendCode(@RequestParam String email){
        authService.sendCode(email);
    }

    @PostMapping("/password")
    public void changePassword(@RequestBody ChangePasswordRequest request){
        if (!redisUtils.getData(request.getCode()).equals(request.getCode())) throw new InvalidCodeException();
        authService.changePassword(request);
    }




    @PostMapping("/signup")
    public TokenResponse signup(@Valid @RequestBody StudentSignupRequest signupRequest){
        return authService.signup(signupRequest);
    }

    @PostMapping("/signup/teacher")
    public TokenResponse teacherSignup(@Valid @RequestBody TeacherSignupRequest request){
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




    @GetMapping("/member/class")
    public List<MemberResponse> getMemberListByClass(@RequestBody SearchQueryRequest searchQueryRequest){
        return authService.getMember(MemberSearchRequest.CLASS, searchQueryRequest);
    }

    @GetMapping("/member/club")
    public List<MemberResponse> getMemberListByClub(@RequestBody SearchQueryRequest searchQueryRequest){
        return authService.getMember(MemberSearchRequest.CLUB, searchQueryRequest);
    }

    @GetMapping("/member/grade")
    public List<MemberResponse> getMemberListByGrade(@RequestBody SearchQueryRequest searchQueryRequest){
        return authService.getMember(MemberSearchRequest.GRADE, searchQueryRequest);
    }






    @PostMapping("/member")
    public MemberResponse getSepMember(@RequestParam String email){
        return authService.getSepMember(email);
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



//서비스 시 해당 api 삭제
    @GetMapping("/admin")
    public void buildAdminMember(@RequestParam String id, @RequestParam String password){
        memberRepository.save(Member.builder()
                        .id(id)
                        .password(passwordEncoder.encode(password))
                        .role(Role.ROLE_ADMIN)
                        .build()
        );
    }


    @GetMapping("/code")
    public List<String> getGeneCode(@RequestParam int count){
        return authService.geneTeacherCode(count);
    }


}
