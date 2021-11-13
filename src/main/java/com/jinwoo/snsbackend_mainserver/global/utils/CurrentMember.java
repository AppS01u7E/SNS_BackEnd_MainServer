package com.jinwoo.snsbackend_mainserver.global.utils;


import com.jinwoo.snsbackend_mainserver.domain.auth.dao.MemberRepository;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentMember {
    private final MemberRepository memberRepository;

    public String getMemberPk(){
        return (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }

    public Member getMember(){
        return memberRepository.findById(getMemberPk()).orElseThrow(MemberNotFoundException::new);
    }


}
