package com.jinwoo.snsbackend_mainserver.domain.auth.controller;


import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.SignupRequest;
import com.jinwoo.snsbackend_mainserver.global.security.payload.TokenResponse;
import com.jinwoo.snsbackend_mainserver.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public TokenResponse signup(@RequestBody SignupRequest signupDto){

    }


}
