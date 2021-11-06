package com.jinwoo.snsbackend_mainserver.global.security;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Role;
import com.jinwoo.snsbackend_mainserver.global.security.payload.TokenResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenProvider {


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

    public boolean VerifyToken(String token){
        Date now = new Date();
        Date expireTime = Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody().getExpiration();
        try {
            expireTime
        }

    }



}
