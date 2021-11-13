package com.jinwoo.snsbackend_mainserver.domain.auth.service;

import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.LoginRequest;
import com.jinwoo.snsbackend_mainserver.domain.auth.payload.request.StudentSignupRequest;
import com.jinwoo.snsbackend_mainserver.global.security.payload.TokenResponse;

public interface AuthService {

    TokenResponse signup(StudentSignupRequest signupRequest);
    TokenResponse login(LoginRequest loginRequest);

}
