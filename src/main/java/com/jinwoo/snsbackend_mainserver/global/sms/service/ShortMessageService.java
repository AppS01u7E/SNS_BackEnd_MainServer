package com.jinwoo.snsbackend_mainserver.global.sms.service;

public interface ShortMessageService {

    String sendSMS(String toNumber, String randomNumber);

}
