package com.jinwoo.snsbackend_mainserver.global.sms.service;

import com.jinwoo.snsbackend_mainserver.global.sms.SmsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class ShortMessageServiceImpl implements ShortMessageService{

    @Value("${coolsms.devHee.apikey}")
    private String apiKey;

    @Value("${coolsms.devHee.apisecret}")
    private String apiSecret;

    @Value("${coolsms.devHee.fromnumber}")
    private String fromNumber;

    @Override
    public String sendSMS(String toNumber, String randomNumber) {

        Message coolsms = new Message(apiKey, apiSecret);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", toNumber);
        params.put("from", fromNumber);
        params.put("type", "SMS");
        params.put("text", "[SNS]\n 인증번호는 "+randomNumber+" 를 입력하세요.");
        params.put("app_version", "demo app 1.3"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            log.info(obj.toString());
            return obj.toString();
        } catch (CoolsmsException e) {
            log.info(e.getMessage());
            throw new SmsException(e);
        }
    }
}
