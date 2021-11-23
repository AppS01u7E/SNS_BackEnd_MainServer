package com.jinwoo.snsbackend_mainserver.global.firebase.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.jinwoo.snsbackend_mainserver.global.firebase.FcmMessage;
import com.jinwoo.snsbackend_mainserver.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@Component
public class FcmServiceImpl implements FcmService{
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/schoolnetworkservice/messages:send";
    private final ObjectMapper objectMapper;
    private final RedisUtil redisUtil;

    @Override
    public String sendMessageTo(String targetId, String title, String body) throws IOException {
        String targetToken = null;
        String message = null;
        try {
            targetToken = redisUtil.getData(targetId + "devT");
            message = makeMessage(targetToken, title, body);
        } catch (NullPointerException e) {
            log.info(targetId+"에게 푸시알림 전송이 실패하였습니다. 로그인 기록이 존재하지 않습니다.");
            return "Failed";
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request)
                .execute();
        return response.message();
    }

    @Override
    public void sendMessageRangeTo(List<String> receiverIds, String title, String body) throws IOException {
        for (String i: receiverIds){
            sendMessageTo(i, title, body);
        }
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        )
                        .build()
                )
                .validate_only(false)
                .build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "schoolnetworkservice-firebase-adminsdk-9pvlk-fda6977dec.json";
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

}
