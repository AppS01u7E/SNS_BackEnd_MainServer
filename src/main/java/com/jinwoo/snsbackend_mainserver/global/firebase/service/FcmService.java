package com.jinwoo.snsbackend_mainserver.global.firebase.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface FcmService {
    public void sendMessageTo(String targetToken, String title, String body) throws IOException;
}
