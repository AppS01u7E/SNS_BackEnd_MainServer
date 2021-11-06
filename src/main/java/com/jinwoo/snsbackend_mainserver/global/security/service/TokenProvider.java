package com.jinwoo.snsbackend_mainserver.global.security.service;

import com.jinwoo.snsbackend_mainserver.domain.auth.dao.MemberRepository;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Role;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.MemberNotFoundException;
import com.jinwoo.snsbackend_mainserver.global.security.component.CustomUserDetailsService;
import com.jinwoo.snsbackend_mainserver.global.security.payload.TokenResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final MemberRepository memberRepository;

    @Value("${security.expiredTime.accessToken}")
    private Long ACCESSTOKEN_EXPIRED;
    @Value("${security.expiredTime.refreshToken}")
    private Long REFRESHTOKEN_EXPIRED;
    @Value("${security.secret}")
    private String SECRETKEY;


    public TokenResponse CreateToken(String id, Role role){

        Claims claims = Jwts.claims().setSubject(id).setExpiration(new Date(System.currentTimeMillis() + ACCESSTOKEN_EXPIRED)).setAudience(role.toString());

        return TokenResponse.builder()
                .accessToken(
                    Jwts.builder()
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.HS256, SECRETKEY)
                    .setExpiration(new Date(System.currentTimeMillis() + ACCESSTOKEN_EXPIRED))
                    .compact()
                )
                .refreshToken(
                        Jwts.builder()
                                .signWith(SignatureAlgorithm.HS256, SECRETKEY)
                                .setExpiration(new Date(System.currentTimeMillis() + REFRESHTOKEN_EXPIRED))
                                .compact()
                )
                .build();
    }

    public boolean verifyToken(String token){
        Date now = new Date();
        Date expireTime = Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody().getExpiration();

        return expireTime.after(now);
    }


    public String getUserId(String token){
        return Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody().getSubject();
    }

    public Authentication getAuthentication(String token){
        String id = Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody().getSubject();
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return new UsernamePasswordAuthenticationToken(member.getId(), member.getRole());
    }



}
