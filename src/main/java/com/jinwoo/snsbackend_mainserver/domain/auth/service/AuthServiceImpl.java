package com.jinwoo.snsbackend_mainserver.domain.auth.service;

import com.jinwoo.snsbackend_mainserver.domain.auth.dao.MemberRepository;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Gender;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Role;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.School;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.*;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.*;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.response.Badge;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.response.MemberResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.dao.SoomRepository;
import com.jinwoo.snsbackend_mainserver.domain.soom.exception.SoomNotFoundException;
import com.jinwoo.snsbackend_mainserver.global.email.service.EmailService;
import com.jinwoo.snsbackend_mainserver.global.exception.AlreadyExistsInListException;
import com.jinwoo.snsbackend_mainserver.global.exception.DataCannotBringException;
import com.jinwoo.snsbackend_mainserver.global.exception.NotExistsInListException;
import com.jinwoo.snsbackend_mainserver.global.security.payload.TokenResponse;
import com.jinwoo.snsbackend_mainserver.global.security.service.TokenProvider;
import com.jinwoo.snsbackend_mainserver.global.utils.CurrentMember;
import com.jinwoo.snsbackend_mainserver.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final CurrentMember currentMember;
    private final RedisUtil redisUtil;
    private final SoomRepository soomRepository;
    private final EmailService emailService;


    @Override
    public void checkEmail(String email) {
        if (memberRepository.findById(email).isPresent()) throw new MemberAlreadyExistsException();
        if (!email.endsWith("dsm.hs.kr")) throw new NotSchoolEmailException();
    }

    @Override
    public void sendCode(String email) {
        String random = UUID.randomUUID().toString().toUpperCase(Locale.ROOT).substring(0, 6);
        memberRepository.findById(email).orElseThrow(MemberNotFoundException::new);
        redisUtil.setDataExpire(email+"password", random, 300);
        try{
            emailService.sendEmail(email, random);

        } catch (IOException | MessagingException e){
            throw new DataCannotBringException();
        }

    }


    @Override
    public void changePassword(ChangePasswordRequest request) {
        memberRepository.save(currentMember.getMember().changePassword(request.getPassword()));
    }

    @Override
    public TokenResponse signup(StudentSignupRequest r) {
        Member member = createMember(r.getId(), r.getNickName(), r.getPassword(), r.getGender(), r.getBirth(), r.getGrade(), r.getClassNum(), r.getNumber(), School.DAEDOK,
                r.getName(), Role.ROLE_STUDENT);

        return tokenProvider.createToken(member.getId(), member.getRole());
    }

    @Override
    public TokenResponse teacherSignup(TeacherSignupRequest r) {
        try {
            log.info(r.getTeacherCode());
            log.info(redisUtil.getData(r.getTeacherCode()));
            if (!redisUtil.getData(r.getTeacherCode()).equals("ACCESS")) throw new InvalidCodeException();
        } catch (NullPointerException e) {
            throw new InvalidCodeException();
        }
        Member member = createMember(r.getId(), r.getNickName(), r.getPassword(), r.getGender(), r.getBirth(), r.getGrade(), r.getClassNum(), r.getNumber(), School.DAEDOK,
                r.getName(), Role.ROLE_TEACHER);
        return tokenProvider.createToken(member.getId(), member.getRole());
    }

    private Member createMember(String id, String nickName, String password, Gender gender, LocalDate birth, int grade, int classNum, int number, School school
    , String name, Role role){

        checkEmail(id);

        Member member = Member.builder()
                .id(id)
                .nickName(nickName)
                .password(passwordEncoder.encode(password))
                .gender(gender)
                .birth(birth)
                .grade(grade)
                .classNum(classNum)
                .number(number)
                .soomRooms(new ArrayList<>())
                .school(School.DAEDOK)
                .name(name)
                .role(role)
                .isLocked(false)
                .build();

        memberRepository.save(
                member
        );
        log.info(member.getId() + "  회원가입 성공");
        return member;
    }





    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findById(loginRequest.getId()).orElseThrow(MemberNotFoundException::new);
        if (passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())){
            try{
                if (!loginRequest.getDeviceToken().isBlank()){
                    redisUtil.setData(loginRequest.getId() + "devT", loginRequest.getDeviceToken());
                    log.info("devide Token:" + loginRequest.getDeviceToken());
                }
            } catch (NullPointerException e) {
                return tokenProvider.createToken(member.getId(), member.getRole());
            }
        }
        throw new IncorrectPasswordException();
    }



     @Override
    public List<MemberResponse> getMember(MemberSearchRequest memberSearchRequest, SearchQueryRequest request) {
        List<MemberResponse> memberResponseList = new ArrayList<>();

        if (memberSearchRequest.equals(MemberSearchRequest.GRADE)){
            memberRepository.findAllBySchoolAndGrade(currentMember.getMember().getSchool(), request.getGrade()).forEach(
                    member -> memberResponseList.add(memberToMemberResponse(member))
            );
        }
        else if (memberSearchRequest.equals(MemberSearchRequest.CLASS)){
            memberRepository.findAllBySchoolAndGradeAndClassNum(currentMember.getMember().getSchool(), request.getGrade(), request.getClassNum()).forEach(
                    member -> memberResponseList.add(memberToMemberResponse(member))
            );
        }
        else{
            soomRepository.findById(request.getSoomId()).orElseThrow(SoomNotFoundException::new).getMemberIds().forEach(
                    s -> memberResponseList.add(memberToMemberResponse(memberRepository.findById(s).orElseThrow(MemberNotFoundException::new)))
            );
        }

        return memberResponseList;
    }

    @Override
    public TokenResponse reissue(TokenResponse tokenResponse){
        return tokenProvider.reissue(tokenResponse);
    }


    @Override
    public MemberResponse mypage() {
        Member member = currentMember.getMember();

        MemberResponse memberResponse = memberToMemberResponse(member);
        if (member.getRole().equals(Role.ROLE_TEACHER))
            memberResponse.setBadge(Badge.TEACHER);
        else if (soomRepository.existsByRepresentativeId(member.getId())){
            memberResponse.setBadge(Badge.SOOM_HEAD);
        }
        else
            memberResponse.setBadge(Badge.COMMON);

        return memberResponse;
    }

    @Override
    public MemberResponse editMypage(EditMypageRequest request) {
        Member member = memberRepository.save(
                currentMember.getMember().editMypage(request)
        );
        return memberToMemberResponse(member);
    }


    @Override
    public MemberResponse getSepMember(String memberId) {
        Member member = memberRepository.findBySchoolAndId(currentMember.getMember().getSchool()
                , memberId).orElseThrow(MemberNotFoundException::new);
        return memberToMemberResponse(member);
    }

    @Override
    public List<String> geneTeacherCode(int i) {
        List<String> strings = new ArrayList<>();
        for (int j = 0; j < i; j++) {

            String random = UUID.randomUUID().toString().substring(0, 6);
            redisUtil.setDataExpire(random, "ACCESS", 3600*24*3);
            strings.add(random);
        }
        return strings;
    }


    @Override
    public void ignoreSoomChat(String soomRoomId) {
        if (!currentMember.getMember().getChatIgnoreList().contains(soomRoomId)) throw new AlreadyExistsInListException();

        memberRepository.save(soomRepository.findByIdAndMemberIdsContains(soomRoomId, currentMember.getMemberPk()).map(
                soomRoom -> currentMember.getMember().addChatIgnoreList(soomRoomId)
        ).orElseThrow(SoomNotFoundException::new));
    }

    @Override
    public void ignoreSoomNotice(String soomRoomId) {
        if (!currentMember.getMember().getChatIgnoreList().contains(soomRoomId)) throw new AlreadyExistsInListException();

        memberRepository.save(soomRepository.findByIdAndMemberIdsContains(soomRoomId, currentMember.getMemberPk()).map(
                soomRoom -> currentMember.getMember().addNoticeIgnorList(soomRoom.getId())
        ).orElseThrow(SoomNotFoundException::new));
    }

    @Override
    public void headSoomChat(String soomRoomId) {
        if (currentMember.getMember().getChatIgnoreList().contains(soomRoomId)) throw new NotExistsInListException();

        memberRepository.save(soomRepository.findByIdAndMemberIdsContains(soomRoomId, currentMember.getMemberPk()).map(
            soomRoom -> currentMember.getMember().removeObjectFromChatIgnoreList(soomRoom.getId())
        ).orElseThrow(SoomNotFoundException::new));
    }

    @Override
    public void headSoomNotice(String soomRoomId) {
        if (currentMember.getMember().getNoticeIgnoreList().contains(soomRoomId)) throw new NotExistsInListException();
        memberRepository.save(soomRepository.findByIdAndMemberIdsContains(soomRoomId, currentMember.getMemberPk()).map(
                soomRoom -> currentMember.getMember().removeObjectFromNoticeIgnoreList(soomRoom.getId())
        ).orElseThrow(SoomNotFoundException::new));
    }


    private MemberResponse memberToMemberResponse(Member member){
        return MemberResponse.builder()
                .gender(member.getGender())
                .birth(member.getBirth())
                .name(member.getName())
                .id(member.getId())
                .grade(member.getGrade())
                .classNum(member.getClassNum())
                .number(member.getNumber())
                .ignoredChatAlarmList(member.getChatIgnoreList())
                .ignoredNoticeAlarmList(member.getNoticeIgnoreList())
                .build();
    }
}
