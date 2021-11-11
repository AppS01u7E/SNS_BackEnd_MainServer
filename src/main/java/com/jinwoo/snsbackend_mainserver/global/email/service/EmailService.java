package com.jinwoo.snsbackend_mainserver.global.email.service;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


public interface EmailService {
    public String sendEmail(String email, String random) throws MessagingException, UnsupportedEncodingException;
}
