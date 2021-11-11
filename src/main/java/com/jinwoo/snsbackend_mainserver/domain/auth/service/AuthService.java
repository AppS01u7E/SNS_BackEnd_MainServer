package com.jinwoo.snsbackend_mainserver.domain.auth.service;

import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.LoginRequest;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.SignupRequest;
import com.jinwoo.snsbackend_mainserver.global.security.payload.TokenResponse;

public interface AuthService {

    TokenResponse signup(SignupRequest signupRequest);
    TokenResponse login(LoginRequest loginRequest);

}
