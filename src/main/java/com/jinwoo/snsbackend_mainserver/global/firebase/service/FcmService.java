package com.jinwoo.snsbackend_mainserver.global.firebase.service;

import java.io.IOException;
import java.util.List;

public interface FcmService {
    public String sendMessageTo(String targetToken, String title, String body) throws IOException;
    public void sendMessageRangeTo(List<String> receiverIds, String title, String body) throws IOException;
}
