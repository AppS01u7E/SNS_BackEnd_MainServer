package com.jinwoo.snsbackend_mainserver.global.email.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender javaMailSender;

    @Value("${email.name}")
    private String to;
    @Value("${email.password}")
    private String from;

    @Override
    public String sendEmail(String email, String random) throws MessagingException, UnsupportedEncodingException {

        StringBuilder body = new StringBuilder();
        body.append("<html> <body><h1>Hello </h1>");
        body.append("<div>인증번호는 '" + random + "'. <img src=\"cid:flower.jpg\"> </div> </body></html>");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");

        mimeMessageHelper.setFrom(from,"hshan");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject("<<SNS>> 인증번호입니다.");
        mimeMessageHelper.setText(body.toString(), true);

        javaMailSender.send(message);
        return random;
    }
}
